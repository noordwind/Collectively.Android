package pl.adriankremski.collectively.presentation.util

import android.graphics.Color

class MaterialColorsTemplate {

    private var nextColor = 0

    fun nextColor(): Int {
        if (nextColor == MATERIAL_DESIGN_COLORS.size) {
            nextColor = 0
        }
        return MATERIAL_DESIGN_COLORS[nextColor++]
    }

    companion object {

        val MATERIAL_DESIGN_COLORS = intArrayOf(Color.parseColor("#F44336"), // red
                Color.parseColor("#2196F3"), // blue
                Color.parseColor("#4CAF50"), // green
                Color.parseColor("#FDD835"), // yellow
                Color.parseColor("#FF6F00"), // orange
                Color.parseColor("#9C27B0"), // purple
                Color.parseColor("#009688"), // teal
                Color.parseColor("#00BCD4"), // cyan
                Color.parseColor("#673AB7"), // deep purple
                Color.parseColor("#03A9F4"), // light blue
                Color.parseColor("#FFC107"), // amber
                Color.parseColor("#E91E63"))// pink
    }
}
