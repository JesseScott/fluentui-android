/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.fluentui.contextualcommandbar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.microsoft.fluentui.ccb.R
import com.microsoft.fluentui.util.isVisible

internal class CommandItemAdapter(
    private var options: CommandListOptions
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var flattenCommandItems = arrayListOf<CommandItem>()
    var commandItemGroups = arrayListOf<CommandItemGroup>()
        set(value) {
            field = value

            flatItemGroup()
        }

    var itemClickListener: CommandItem.OnItemClickListener? = null
    var itemLongClickListener: CommandItem.OnItemLongClickListener? = null

    fun addItemGroup(itemGroup: CommandItemGroup) {
        commandItemGroups.add(itemGroup)

        flatItemGroup()
    }

    fun setGroupSpace(space: Int) {
        options.groupSpace = space
    }

    fun setItemSpace(space: Int) {
        options.itemSpace = space
    }

    private fun flatItemGroup() {
        flattenCommandItems.clear()
        commandItemGroups.forEach {
            flattenCommandItems.addAll(it.items)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.view_command_item, parent, false)
        )
    }

    override fun onBindViewHolder(vh: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        val viewHolder = vh as ViewHolder
        val commandItem = flattenCommandItems[position]
        val isItemSelected = commandItem.isSelected()
        val isItemEnabled = commandItem.isEnabled()
        val label = commandItem.getLabel()
        val icon = commandItem.getIcon()
        val bitmapIcon = commandItem.getBitmapIcon()
        val description = commandItem.getContentDescription()
        (commandItem as DefaultCommandItem).setView(vh.itemView)
        if (icon != 0) {
            // Using icon as primary display content
            viewHolder.label.isVisible = false
            with(viewHolder.icon) {
                isVisible = true
                setImageResource(icon)
                imageTintList = AppCompatResources.getColorStateList(
                    context,
                    R.color.contextual_command_bar_icon_tint
                )
                contentDescription = description
                isEnabled = isItemEnabled
                isSelected = isItemSelected
            }

        } else if (bitmapIcon != null) {
            viewHolder.label.isVisible = false
            with(viewHolder.icon) {
                isVisible = true
                setImageBitmap(bitmapIcon)
                contentDescription = description
                isEnabled = isItemEnabled
                isSelected = isItemSelected
            }
        } else if (!label.isNullOrEmpty()) {
            viewHolder.icon.isVisible = false
            with(viewHolder.label) {
                isVisible = true
                text = label
                contentDescription = description
                isEnabled = isItemEnabled
                isSelected = isItemSelected
            }
        } else {
            // Return if no icon and label
            return
        }

        // Update the UI of command item
        with(viewHolder.itemView) {
            isEnabled = isItemEnabled
            isSelected = isItemSelected
            when (viewType) {
                VIEW_TYPE_GROUP_CENTER_ITEM -> {
                    (layoutParams as RecyclerView.LayoutParams).apply {
                        marginEnd = options.itemSpace
                    }

                    background = ContextCompat.getDrawable(
                        context,
                        R.drawable.contextual_command_bar_center_item_background
                    )
                }

                VIEW_TYPE_GROUP_START_ITEM -> {
                    (layoutParams as RecyclerView.LayoutParams).apply {
                        marginEnd = options.itemSpace
                    }
                    background = ContextCompat.getDrawable(
                        context,
                        R.drawable.contextual_command_bar_start_item_background
                    )
                }

                VIEW_TYPE_GROUP_END_ITEM -> {
                    (layoutParams as RecyclerView.LayoutParams).apply {
                        marginEnd = if (position == flattenCommandItems.size - 1) 0
                        else options.groupSpace
                    }

                    background = ContextCompat.getDrawable(
                        context,
                        R.drawable.contextual_command_bar_end_item_background
                    )
                }

                VIEW_TYPE_GROUP_SINGLE_ITEM -> {
                    (layoutParams as RecyclerView.LayoutParams).apply {
                        marginEnd = if (position == flattenCommandItems.size - 1) 0
                        else options.groupSpace
                    }
                    background = ContextCompat.getDrawable(
                        context,
                        R.drawable.contextual_command_bar_single_item_background
                    )
                }
            }
            setOnLongClickListener {
                itemLongClickListener?.onItemLongClick(commandItem, viewHolder.itemView)!!
            }
            setOnClickListener {
                itemClickListener?.onItemClick(commandItem, viewHolder.itemView)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (flattenCommandItems.size == 0) {
            return super.getItemViewType(position)
        }
        var pendingSearch = position + 1
        for (group in commandItemGroups) {
            val itemsSize = group.items.size
            if (itemsSize == 0) {
                continue
            }

            if (pendingSearch > itemsSize) {
                pendingSearch -= itemsSize
                continue
            } else {
                return when {
                    itemsSize == 1 -> {
                        VIEW_TYPE_GROUP_SINGLE_ITEM
                    }
                    pendingSearch == 1 -> {
                        VIEW_TYPE_GROUP_START_ITEM
                    }
                    pendingSearch == itemsSize -> {
                        VIEW_TYPE_GROUP_END_ITEM
                    }
                    else -> {
                        VIEW_TYPE_GROUP_CENTER_ITEM
                    }
                }
            }
        }

        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int = flattenCommandItems.size

    private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.contextual_command_item_icon)
        val label: TextView = itemView.findViewById(R.id.contextual_command_item_label)
    }

    data class CommandListOptions(
        var groupSpace: Int,
        var itemSpace: Int
    )

    companion object {
        const val VIEW_TYPE_GROUP_CENTER_ITEM = 0
        const val VIEW_TYPE_GROUP_START_ITEM = 1
        const val VIEW_TYPE_GROUP_END_ITEM = 2
        const val VIEW_TYPE_GROUP_SINGLE_ITEM = 3
    }
}
