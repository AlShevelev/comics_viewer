package com.shevelev.comics_viewer.activities.view_comics

import android.app.Activity
import android.content.Context
import android.graphics.PointF
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import com.shevelev.comics_viewer.activities.view_comics.helpers.PointsHelper.getDistance
import com.shevelev.comics_viewer.activities.view_comics.user_actions_managing.IUserActionsManaged
import com.shevelev.comics_viewer.activities.view_comics.user_actions_managing.UserActionManager
import com.shevelev.comics_viewer.activities.view_comics.user_actions_managing.ViewStateCodes
import com.shevelev.comics_viewer.common.helpers.ScreenHelper

/**
 * OpenGL ES View.
 *
 * @author harism
 */
class CurlView : GLSurfaceView, OnTouchListener, CurlRenderer.Observer, IUserActionsManaged {
    private var currentContext: Context? = null
    private var curlState = CurlState.None // Curl state. We are flipping none, left or right page.
    private var animationTargetEvent = CurlTarget.None
    private var canCurlLastPage = false // Can we curl last page
    private var animate = false
    private val animationDurationTime: Long = 300
    private val animationSource = PointF()
    private var animationStartTime: Long = 0
    private val animationTarget = PointF()
    private val curlDir = PointF()
    private val curlPos = PointF()
    private var currentPageIndex = 0 // Current bitmap index. This is always showed as front of right page. = 0
    // Start position for dragging.
    private val dragStartPos = PointF()
    private val enableTouchPressure = false
    // Bitmap size. These are updated from renderer once it's initialized.
    private var pageBitmapHeight = -1
    private var pageBitmapWidth = -1
    // Page meshes. Left and right meshes are 'static' while curl is used to
// show page flipping.
    private var pageCurl: CurlMesh? = null
    private var pageLeft: CurlMesh? = null
    private var pageProvider: IPageProvider? = null
    private var pageRight: CurlMesh? = null
    private val pointerPos = PointerPosition()
    private var renderer: CurlRenderer? = null
    private val renderLeftPage = true
    private var userActionManager: UserActionManager? = null
    private var resizingState: ResizingState? = null
    private var resizingPointsDistance // Distance between points while resizing
        : Float? = null
    private var viewStateCodes = ViewStateCodes.NotResized
    private var screenDiagonal = 0f // Size of screen diagonal in pixels = 0f
    private var firstDraggingPoint // Size of screen diagonal in pixels
        : PointF? = null
    private var draggingState: DraggingState? = null
    private var onPageChanged: ((Int) -> Unit)? = null

    // When we need show menu
    private var onShowMenu : (() -> Unit)? = null

    /**
     * Default constructor.
     */
    constructor(context: Context) : super(context) {
        init(context)
    }

    /**
     * Default constructor.
     */
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    /**
     * Default constructor.
     */
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : this(context, attrs) {}

    /**
     * Initialize method.
     */
    private fun init(ctx: Context) {
        currentContext = ctx
        userActionManager = UserActionManager(this, ScreenHelper.getScreenSize(currentContext as Activity))
        renderer = CurlRenderer(this)
        setRenderer(renderer)
        renderMode = RENDERMODE_WHEN_DIRTY
        setOnTouchListener(this)
        // Even though left and right pages are static we have to allocate room
// for curl on them too as we are switching meshes. Another way would be
// to swap texture ids only.
        pageLeft = CurlMesh(10)
        pageRight = CurlMesh(10)
        pageCurl = CurlMesh(10)
        pageLeft!!.setFlipTexture(true)
        pageRight!!.setFlipTexture(false)
        draggingState = DraggingState(ResizingState.MIN_MARGIN, ResizingState.MAX_MARGIN)
    }

    override fun onDrawFrame() { // We are not animating.
        if (animate == false) return
        val currentTime = System.currentTimeMillis()
        // If animation is done.
        if (currentTime >= animationStartTime + animationDurationTime) {
            if (animationTargetEvent === CurlTarget.ToRight) { // Switch curled page to right.
                val right = pageCurl
                val curl = pageRight
                right!!.setRect(renderer!!.getPageRect(CurlState.Right)!!)
                right.setFlipTexture(false)
                right.reset()
                renderer!!.removeCurlMesh(curl)
                pageCurl = curl
                pageRight = right
                // If we were curling left page update current index.
                if (curlState === CurlState.Left) --currentPageIndex
            } else if (animationTargetEvent === CurlTarget.ToLeft) { // Switch curled page to left.
                val left = pageCurl
                val curl = pageLeft
                left!!.setRect(renderer!!.getPageRect(CurlState.Left)!!)
                left.setFlipTexture(true)
                left.reset()
                renderer!!.removeCurlMesh(curl)
                if (!renderLeftPage) {
                    renderer!!.removeCurlMesh(left)
                }
                pageCurl = curl
                pageLeft = left
                // If we were curling right page update current index.
                if (curlState === CurlState.Right) ++currentPageIndex
            }
            curlState = CurlState.None
            animate = false
            onPageChanged!!(currentPageIndex)
            requestRender()
        } else {
            pointerPos.mPos.set(animationSource)
            var t = 1f - (currentTime - animationStartTime).toFloat() / animationDurationTime
            t = 1f - t * t * t * (3 - 2 * t)
            pointerPos.mPos.x += (animationTarget.x - animationSource.x) * t
            pointerPos.mPos.y += (animationTarget.y - animationSource.y) * t
            updateCurlPos(pointerPos)
        }
    }

    override fun onPageSizeChanged(width: Int, height: Int) {
        pageBitmapWidth = width
        pageBitmapHeight = height
        updatePages()
        //requestRender();
    }

    public override fun onSizeChanged(w: Int, h: Int, ow: Int, oh: Int) {
        super.onSizeChanged(w, h, ow, oh)
        reset()
        screenDiagonal = Math.sqrt(Math.pow(w.toDouble(), 2.0) + Math.pow(h.toDouble(), 2.0)).toFloat()
    }

    /**
     * Reset state of current page - cancel resizing and so on
     */
    private fun reset() {
        renderer!!.setDragging(draggingState!!.reset()) // Reset dragging
        requestRender()
        pageLeft!!.setFlipTexture(true)
        renderer!!.setViewMode()
        requestRender()
        resizingState = ResizingState(Margins(0f, 0f, 0f, 0f), 1f) // Original size
        renderer!!.setMargins(resizingState!!.margins!!)
        renderer!!.setScale(resizingState!!.scaleFactor)
        viewStateCodes = if (resizingState!!.isResized) ViewStateCodes.Resized else ViewStateCodes.NotResized
    }

    override fun onSurfaceCreated() { // In case surface is recreated, let page meshes drop allocated texture
// ids and ask for new ones. There's no need to set textures here as
// onPageSizeChanged should be called later on.
        pageLeft!!.resetTexture()
        pageRight!!.resetTexture()
        pageCurl!!.resetTexture()
    }

    private fun memorizePoint(x: Float, y: Float, pressure: Float) {
        pointerPos.mPos[x] = y
        renderer!!.translate(pointerPos.mPos)
        if (enableTouchPressure) pointerPos.mPressure = pressure else pointerPos.mPressure = 0.8f
    }

    override fun startCurving(point: PointF, pressure: Float) {
        val rightRect = renderer!!.getPageRect(CurlState.Right)
        memorizePoint(point.x, point.y, pressure)
        // Once we receive pointer down event its position is mapped to
// right or left edge of page and that'll be the position from where
// user is holding the paper to make curl happen.
        dragStartPos.set(pointerPos.mPos)
        // First we make sure it's not over or below page. Pages are
// supposed to be same height so it really doesn't matter do we use
// left or right one.
        if (dragStartPos.y > rightRect!!.top) {
            dragStartPos.y = rightRect.top
        } else if (dragStartPos.y < rightRect.bottom) {
            dragStartPos.y = rightRect.bottom
        }
        // Then we have to make decisions for the user whether curl is going
// to happen from left or right, and on which page.
        val halfX = (rightRect.right + rightRect.left) / 2
        if (dragStartPos.x < halfX && currentPageIndex > 0) {
            dragStartPos.x = rightRect.left
            startCurl(CurlState.Left)
        } else if (dragStartPos.x >= halfX && currentPageIndex < pageProvider!!.pageCount) {
            dragStartPos.x = rightRect.right
            if (!canCurlLastPage && currentPageIndex >= pageProvider!!.pageCount - 1) return
            startCurl(CurlState.Right)
        }
        // If we have are in curl state, let this case clause flow through
// to next one. We have pointer position and drag position defined
// and this will setDiskItems first render request given these points.
        if (curlState === CurlState.None) return
    }

    override fun curving(point: PointF, pressure: Float) {
        memorizePoint(point.x, point.y, pressure)
        updateCurlPos(pointerPos)
    }

    override fun completeCurving(point: PointF, pressure: Float) {
        val rightRect = renderer!!.getPageRect(CurlState.Right)
        val leftRect = renderer!!.getPageRect(CurlState.Left)
        memorizePoint(point.x, point.y, pressure)
        if (curlState === CurlState.Left || curlState === CurlState.Right) { // Animation source is the point from where animation starts.
// Also it's handled in a way we actually simulate touch events
// meaning the output is exactly the same as if user drags the
// page to other side. While not producing the best looking
// result (which is easier done by altering curl position and/or
// direction directly), this is done in a hope it made code a
// bit more readable and easier to maintain.
            animationSource.set(pointerPos.mPos)
            animationStartTime = System.currentTimeMillis()
            // Given the explanation, here we decide whether to simulate
// drag to left or right end.
            if (pointerPos.mPos.x > (rightRect!!.left + rightRect.right) / 2) { // On right side target is always right page's right border.
                animationTarget.set(dragStartPos)
                animationTarget.x = renderer!!.getPageRect(CurlState.Right)!!.right
                animationTargetEvent = CurlTarget.ToRight
            } else { // On left side target depends on visible pages.
                animationTarget.set(dragStartPos)
                if (curlState === CurlState.Right) animationTarget.x = leftRect!!.left else animationTarget.x = rightRect.left
                animationTargetEvent = CurlTarget.ToLeft
            }
            animate = true
            renderer!!.setDragging(draggingState!!.reset())
            requestRender()
        }
    }

    override fun cancelCurving(point: PointF, pressure: Float) {
        if (curlState === CurlState.None) return
        memorizePoint(point.x, point.y, pressure)
        animationTarget.set(dragStartPos)
        if (curlState === CurlState.Left) {
            animationTarget.x = renderer!!.getPageRect(CurlState.Left)!!.left
            animationTargetEvent = CurlTarget.ToLeft
        } else if (curlState === CurlState.Right) {
            animationTarget.x = renderer!!.getPageRect(CurlState.Right)!!.right
            animationTargetEvent = CurlTarget.ToRight
        }
        animate = true
        Log.d("DRAGGING", "cancelCurving - reset")
        renderer!!.setDragging(draggingState!!.reset())
        requestRender()
    }

    override fun startResizing() {
        resizingPointsDistance = null
        viewStateCodes = if (resizingState!!.isResized) ViewStateCodes.Resized else ViewStateCodes.NotResized
        renderer!!.setMargins(Margins(0f, 0f, 0f, 0f))
        renderer!!.setScale(resizingState!!.scaleFactor)
        if (viewStateCodes === ViewStateCodes.Resized) renderer!!.setDragging(draggingState!!.reset()) // Place to center
    }

    override fun resizing(points: List<PointF>) {
        val newResizingPointsDistance = getDistance(points)
        if (resizingPointsDistance == null && newResizingPointsDistance > 0f) resizingPointsDistance = newResizingPointsDistance else {
            val resizingFactor = calculateResizingFactor(resizingPointsDistance!!, newResizingPointsDistance)
            resizingState!!.updateScaleFactor(resizingFactor)
            viewStateCodes = if (resizingState!!.isResized) ViewStateCodes.Resized else ViewStateCodes.NotResized
            resizingPointsDistance = newResizingPointsDistance
            renderer!!.setScale(resizingState!!.scaleFactor)
            requestRender() // Update frame
        }
    }

    override fun completeResizing() {
        resizingPointsDistance = null
        viewStateCodes = if (resizingState!!.isResized) ViewStateCodes.Resized else ViewStateCodes.NotResized
        resizingState!!.recalculateMarginsByScaleFactor()
        val margins = resizingState!!.margins
        renderer!!.setMargins(margins!!) // too heavy
        draggingState!!.setCurrentMargins(margins)
        renderer!!.setScale(1f)
        requestRender()
        if (viewStateCodes === ViewStateCodes.NotResized) {
            Log.d("DRAGGING", "completeResizing - reset")
            renderer!!.setDragging(draggingState!!.reset()) // Place to center
            requestRender()
        }
    }

    override fun startDragging(point: PointF) {
        firstDraggingPoint = point
        draggingState!!.setViewInfo(renderer!!.viewInfo)
        draggingState!!.startDragging()
    }

    override fun dragging(point: PointF) {
        val deltaX = point.x - firstDraggingPoint!!.x
        val deltaY = point.y - firstDraggingPoint!!.y
        Log.d("DRAGGING", "dragging")
        renderer!!.setDragging(draggingState!!.processDragging(deltaX, deltaY))
        requestRender() // Update frame
    }

    override fun completeDragging(point: PointF) {
        draggingState!!.completeDragging()
    }

    override fun showMenu() {
        onShowMenu!!()
    }

    override fun onTouch(view: View, me: MotionEvent): Boolean {
        if (animate || pageProvider == null) return false
        userActionManager!!.Process(me, viewStateCodes)
        return true
    }

    /**
     * Calculate factor for changing margins while resizing;
     */
    private fun calculateResizingFactor(oldPointsDistance: Float, newPointsDistance: Float): Float {
        val resizingMultiplier = 6f
        val delta = newPointsDistance - oldPointsDistance
        return resizingMultiplier * (delta / screenDiagonal)
    }

    /**
     * Allow the last page to curl.
     */
    fun setAllowLastPageCurl(allowLastPageCurl: Boolean) {
        canCurlLastPage = allowLastPageCurl
    }

    /**
     * Sets background color - or OpenGL clear color to be more precise. Color
     * is a 32bit value consisting of 0xAARRGGBB and is extracted using
     * android.graphics.Color eventually.
     */
    override fun setBackgroundColor(color: Int) {
        renderer!!.setBackgroundColor(color)
        requestRender()
    }

    /**
     * Sets pageCurl curl position.
     */
    private fun setCurlPos(curlPos: PointF, curlDir: PointF, radius: Double) { // First reposition curl so that page doesn't 'rip off' from book.
        if (curlState === CurlState.Right || curlState === CurlState.Left) {
            val pageRect = renderer!!.getPageRect(CurlState.Right)
            if (curlPos.x >= pageRect!!.right) {
                pageCurl!!.reset()
                requestRender()
                return
            }
            if (curlPos.x < pageRect.left) {
                curlPos.x = pageRect.left
            }
            if (curlDir.y != 0f) {
                val diffX = curlPos.x - pageRect.left
                val leftY = curlPos.y + diffX * curlDir.x / curlDir.y
                if (curlDir.y < 0 && leftY < pageRect.top) {
                    curlDir.x = curlPos.y - pageRect.top
                    curlDir.y = pageRect.left - curlPos.x
                } else if (curlDir.y > 0 && leftY > pageRect.bottom) {
                    curlDir.x = pageRect.bottom - curlPos.y
                    curlDir.y = curlPos.x - pageRect.left
                }
            }
        } else if (curlState === CurlState.Left) {
            val pageRect = renderer!!.getPageRect(CurlState.Left)
            if (curlPos.x <= pageRect!!.left) {
                pageCurl!!.reset()
                requestRender()
                return
            }
            if (curlPos.x > pageRect.right) {
                curlPos.x = pageRect.right
            }
            if (curlDir.y != 0f) {
                val diffX = curlPos.x - pageRect.right
                val rightY = curlPos.y + diffX * curlDir.x / curlDir.y
                if (curlDir.y < 0 && rightY < pageRect.top) {
                    curlDir.x = pageRect.top - curlPos.y
                    curlDir.y = curlPos.x - pageRect.right
                } else if (curlDir.y > 0 && rightY > pageRect.bottom) {
                    curlDir.x = curlPos.y - pageRect.bottom
                    curlDir.y = pageRect.right - curlPos.x
                }
            }
        }
        // Finally normalize direction vector and do rendering.
        val dist = Math.sqrt(curlDir.x * curlDir.x + curlDir.y * curlDir.y.toDouble())
        if (dist != 0.0) {
            curlDir.x /= dist.toFloat()
            curlDir.y /= dist.toFloat()
            pageCurl!!.curl(curlPos, curlDir, radius)
        } else {
            pageCurl!!.reset()
        }
        requestRender()
    }

    /**
     * Set current page index first time
     */
    fun initCurrentPageIndex(currentPageIndex: Int) {
        this.currentPageIndex = currentPageIndex
        updatePages()
        requestRender()
    }

    /**
     * Change index of current page and switch to this page
     */
    fun setCurrentPageIndex(currentPageIndex: Int) {
        this.currentPageIndex = currentPageIndex
        updatePages()
        reset()
        onPageChanged!!(currentPageIndex)
    }

    /**
     * Update/set page provider.
     */
    fun setPageProvider(pageProvider: IPageProvider?) {
        this.pageProvider = pageProvider
    }

    /**
     * Set callback handlers
     */
    fun setCallbackHandlers(onPageChanged: ((Int) -> Unit)?, onShowMenu: (() -> Unit)?) {
        this.onPageChanged = onPageChanged
        this.onShowMenu = onShowMenu
    }

    /**
     * Switches meshes and loads new bitmaps if available. Updated to support 2
     * pages in landscape
     */
    private fun startCurl(curlState: CurlState) {
        when (curlState) {
            CurlState.Right -> {
                // Remove meshes from renderer.
                renderer!!.removeCurlMesh(pageLeft)
                renderer!!.removeCurlMesh(pageRight)
                renderer!!.removeCurlMesh(pageCurl)
                // We are curling right page.
                val curl = pageRight
                pageRight = pageCurl
                pageCurl = curl
                if (currentPageIndex > 0) {
                    pageLeft!!.setFlipTexture(true)
                    pageLeft!!.setRect(renderer!!.getPageRect(CurlState.Left)!!)
                    pageLeft!!.reset()
                    if (renderLeftPage) renderer!!.addCurlMesh(pageLeft!!)
                }
                if (currentPageIndex < pageProvider!!.pageCount - 1) {
                    updatePage(pageRight!!.texturePage, currentPageIndex + 1)
                    pageRight!!.setRect(renderer!!.getPageRect(CurlState.Right)!!)
                    pageRight!!.setFlipTexture(false)
                    pageRight!!.reset()
                    renderer!!.addCurlMesh(pageRight!!)
                }
                // Add curled page to renderer.
                pageCurl!!.setRect(renderer!!.getPageRect(CurlState.Right)!!)
                pageCurl!!.setFlipTexture(false)
                pageCurl!!.reset()
                renderer!!.addCurlMesh(pageCurl!!)
                this.curlState = CurlState.Right
            }
            CurlState.Left -> {
                // Remove meshes from renderer.
                renderer!!.removeCurlMesh(pageLeft)
                renderer!!.removeCurlMesh(pageRight)
                renderer!!.removeCurlMesh(pageCurl)
                // We are curling left page.
                val curl = pageLeft
                pageLeft = pageCurl
                pageCurl = curl
                if (currentPageIndex > 1) {
                    updatePage(pageLeft!!.texturePage, currentPageIndex - 2)
                    pageLeft!!.setFlipTexture(true)
                    pageLeft!!.setRect(renderer!!.getPageRect(CurlState.Left)!!)
                    pageLeft!!.reset()
                    if (renderLeftPage) renderer!!.addCurlMesh(pageLeft!!)
                }
                // If there is something to show on right page add it to renderer.
                if (currentPageIndex < pageProvider!!.pageCount) {
                    pageRight!!.setFlipTexture(false)
                    pageRight!!.setRect(renderer!!.getPageRect(CurlState.Right)!!)
                    pageRight!!.reset()
                    renderer!!.addCurlMesh(pageRight!!)
                }
                // How dragging previous page happens depends on view mode.
                pageCurl!!.setRect(renderer!!.getPageRect(CurlState.Right)!!)
                pageCurl!!.setFlipTexture(false)
                pageCurl!!.reset()
                renderer!!.addCurlMesh(pageCurl!!)
                this.curlState = CurlState.Left
            }
        }
    }

    /**
     * Updates curl position.
     */
    private fun updateCurlPos(pointerPos: PointerPosition) { // Default curl radius.
        var radius = renderer!!.getPageRect(CurlState.Right)!!.width() / 3.toDouble()
        // TODO: This is not an optimal solution. Based on feedback received so
// far; pressure is not very accurate, it may be better not to map
// coefficient to range [0f, 1f] but something like [.2f, 1f] instead.
// Leaving it as is until get my hands on a real device. On emulator
// this doesn't work anyway.
        radius *= Math.max(1f - pointerPos.mPressure, 0f).toDouble()
        // NOTE: Here we set pointerPos to curlPos. It might be a bit confusing
// later to see e.g "curlPos.value1 - dragStartPos.value1" used. But it's
// actually pointerPos we are doing calculations against. Why? Simply to
// optimize code a bit with the cost of making it unreadable. Otherwise
// we had to this in both of the next if-else branches.
        curlPos.set(pointerPos.mPos)
        // If curl happens on right page, or on left page on two page mode,
// we'll calculate curl position from pointerPos.
        if (curlState === CurlState.Right) {
            curlDir.x = curlPos.x - dragStartPos.x
            curlDir.y = curlPos.y - dragStartPos.y
            val dist = Math.sqrt(curlDir.x * curlDir.x + curlDir.y * curlDir.y.toDouble()).toFloat()
            // Adjust curl radius so that if page is dragged far enough on
// opposite side, radius gets closer to zero.
            val pageWidth = renderer!!.getPageRect(CurlState.Right)!!.width()
            var curlLen = radius * Math.PI
            if (dist > pageWidth * 2 - curlLen) {
                curlLen = Math.max(pageWidth * 2 - dist, 0f).toDouble()
                radius = curlLen / Math.PI
            }
            // Actual curl position calculation.
            if (dist >= curlLen) {
                val translate = (dist - curlLen) / 2
                val pageLeftX = renderer!!.getPageRect(CurlState.Right)!!.left
                radius = Math.max(Math.min(curlPos.x - pageLeftX.toDouble(), radius), 0.0)
                curlPos.y -= (curlDir.y * translate / dist).toFloat()
            } else {
                val angle = Math.PI * Math.sqrt(dist / curlLen)
                val translate = radius * Math.sin(angle)
                curlPos.x += (curlDir.x * translate / dist).toFloat()
                curlPos.y += (curlDir.y * translate / dist).toFloat()
            }

        } else if (curlState === CurlState.Left) { // Adjust radius regarding how close to page edge we are.
            val pageLeftX = renderer!!.getPageRect(CurlState.Right)!!.left
            radius = Math.max(Math.min(curlPos.x - pageLeftX.toDouble(), radius), 0.0)
            val pageRightX = renderer!!.getPageRect(CurlState.Right)!!.right
            curlPos.x -= Math.min(pageRightX - curlPos.x.toDouble(), radius).toFloat()
            curlDir.x = curlPos.x + dragStartPos.x
            curlDir.y = curlPos.y - dragStartPos.y
        }
        setCurlPos(curlPos, curlDir, radius)
    }

    /**
     * Updates given CurlPage via PageProvider for page located at index.
     */
    private fun updatePage(page: CurlPage, index: Int) { // First reset page to initial state.
        page.reset()
        // Ask page provider to fill it up with bitmaps and colors.
        pageProvider!!.updatePage(page, pageBitmapWidth, pageBitmapHeight, index)
    }

    /**
     * Updates bitmaps for page meshes.
     */
    private fun updatePages() {
        if (pageProvider == null || pageBitmapWidth <= 0 || pageBitmapHeight <= 0) return
        // Remove meshes from renderer.
        renderer!!.removeCurlMesh(pageLeft)
        renderer!!.removeCurlMesh(pageRight)
        renderer!!.removeCurlMesh(pageCurl)
        var leftIdx = currentPageIndex - 1
        var rightIdx = currentPageIndex
        var curlIdx = -1
        if (curlState === CurlState.Left) {
            curlIdx = leftIdx
            --leftIdx
        } else if (curlState === CurlState.Right) {
            curlIdx = rightIdx
            ++rightIdx
        }
        if (rightIdx >= 0 && rightIdx < pageProvider!!.pageCount) {
            updatePage(pageRight!!.texturePage, rightIdx)
            pageRight!!.setFlipTexture(false)
            pageRight!!.setRect(renderer!!.getPageRect(CurlState.Right)!!)
            pageRight!!.reset()
            renderer!!.addCurlMesh(pageRight!!)
        }
        if (leftIdx >= 0 && leftIdx < pageProvider!!.pageCount) {
            updatePage(pageLeft!!.texturePage, leftIdx)
            pageLeft!!.setFlipTexture(true)
            pageLeft!!.setRect(renderer!!.getPageRect(CurlState.Left)!!)
            pageLeft!!.reset()
            if (renderLeftPage) renderer!!.addCurlMesh(pageLeft!!)
        }
        if (curlIdx >= 0 && curlIdx < pageProvider!!.pageCount) {
            updatePage(pageCurl!!.texturePage, curlIdx)
            if (curlState === CurlState.Right) {
                pageCurl!!.setFlipTexture(true)
                pageCurl!!.setRect(renderer!!.getPageRect(CurlState.Right)!!)
            } else {
                pageCurl!!.setFlipTexture(false)
                pageCurl!!.setRect(renderer!!.getPageRect(CurlState.Left)!!)
            }
            pageCurl!!.reset()
            renderer!!.addCurlMesh(pageCurl!!)
        }
    }
}