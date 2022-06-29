package com.alexs.home.adapters

import androidx.recyclerview.widget.DiffUtil
import com.alexs.domain.model.items.LoanItem

object LoansDiffUtil : DiffUtil.ItemCallback<LoanItem>() {

    override fun areItemsTheSame(oldItem: LoanItem, newItem: LoanItem) =
        oldItem.loanId == newItem.loanId
                && oldItem.state == newItem.state
                && oldItem.until == newItem.until

    override fun areContentsTheSame(oldItem: LoanItem, newItem: LoanItem) = oldItem == newItem

}