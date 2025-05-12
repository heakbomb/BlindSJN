@file:OptIn(ExperimentalLayoutApi::class)

package com.glowstudio.android.blindsjn.ui.components

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import com.glowstudio.android.blindsjn.model.IndustryType
import com.glowstudio.android.blindsjn.model.User

@Composable
fun WritePostCategorySelector(
    user: User,
    selectedCategories: Set<String>,
    onCategorySelected: (Set<String>) -> Unit
) {
    val baseCategories = listOf("누구나", "질문")
    val industryCategories = IndustryType.values().map { it.displayName }
    val enabledIndustries = user.certifiedIndustries.map { it.displayName }
    val allCategories = baseCategories + industryCategories

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        allCategories.forEach { category ->
            val enabled = baseCategories.contains(category) || enabledIndustries.contains(category)
            FilterChip(
                selected = selectedCategories.contains(category),
                onClick = {
                    if (enabled) {
                        val newSet = if (selectedCategories.contains(category))
                            selectedCategories - category
                        else
                            selectedCategories + category
                        onCategorySelected(newSet)
                    }
                },
                enabled = enabled,
                label = { Text(category) }
            )
        }
    }
}
