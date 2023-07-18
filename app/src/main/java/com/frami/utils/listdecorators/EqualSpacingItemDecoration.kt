package com.frami.utils.listdecorators

import android.graphics.Rect
import android.view.View
import androidx.annotation.IntDef
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EqualSpacingItemDecoration : RecyclerView.ItemDecoration {
  private val spacing: Int
  private var displayMode: Int = 0
  private var includeTopBottom = false

  constructor(spacingInDp: Int, @DisplayMode displayMode: Int, includeTopBottom: Boolean = false) {
    this.spacing = spacingInDp
    this.displayMode = displayMode
    this.includeTopBottom = includeTopBottom
  }

  override fun getItemOffsets(
    outRect: Rect,
    view: View,
    parent: RecyclerView,
    state: RecyclerView.State
  ) {
    super.getItemOffsets(outRect, view, parent, state)
    val position = parent.getChildViewHolder(view).adapterPosition
    val itemCount = state.itemCount
    val layoutManager = parent.layoutManager
    if (layoutManager != null) {
      setSpacingForDirection(outRect, layoutManager, position, itemCount)
    }
  }

  private fun setSpacingForDirection(
    outRect: Rect,
    layoutManager: RecyclerView.LayoutManager,
    position: Int,
    itemCount: Int
  ) {

    // Resolve display mode automatically
    if (displayMode == -1) {
      displayMode = resolveDisplayMode(layoutManager)
    }

    when (displayMode) {
      HORIZONTAL -> {
        outRect.left = if (position > 0) spacing else 0
        outRect.right = 0
        outRect.top = 0
        outRect.bottom = 0
      }
      VERTICAL -> {
        outRect.left = 0
        outRect.right = 0
        outRect.top = if (position > 0) spacing else 0
        outRect.bottom = 0
      }
      GRID -> if (layoutManager is GridLayoutManager) {

        val spanCount = layoutManager.spanCount

        if (position >= 0) {
          val column = position % spanCount // item column

          if (includeTopBottom) {
            outRect.left = spacing - column * spacing /
                spanCount // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing /
                spanCount // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
              outRect.top = spacing
            }
            outRect.bottom = spacing // item bottom
          } else {
            outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
            outRect.right = spacing - (column + 1) * spacing /
                spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
              outRect.top = spacing // item top
            }
          }
        } else {
          outRect.left = 0
          outRect.right = 0
          outRect.top = 0
          outRect.bottom = 0
        }
      }
    }
  }

  private fun resolveDisplayMode(layoutManager: RecyclerView.LayoutManager): Int {
    if (layoutManager is GridLayoutManager) return GRID
    return if (layoutManager.canScrollHorizontally()) HORIZONTAL else VERTICAL
  }

  companion object {

    const val HORIZONTAL = 0
    const val VERTICAL = 1
    const val GRID = 2

    @IntDef(
      HORIZONTAL,
      VERTICAL,
      GRID
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class DisplayMode
  }
}