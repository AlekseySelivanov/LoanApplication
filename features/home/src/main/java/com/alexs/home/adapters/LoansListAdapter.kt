package com.alexs.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alexs.domain.model.LoanState
import com.alexs.domain.model.items.LoanItem
import com.alexs.home.R
import com.alexs.home.databinding.ItemLoanBinding

class LoansListAdapter(
    private val onLoanClicked: (loanId: Int) -> Unit
) : ListAdapter<LoanItem, LoansListAdapter.LoansListViewHolder>(LoansDiffUtil) {

    inner class LoansListViewHolder(private val binding: ItemLoanBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemPosition: Int) {
            val loanItem = getItem(itemPosition)
            val context = binding.root.context

            with(binding) {
                tvUntil.text = loanItem.until
                tvToPay.text = tvBankName.context.getString(R.string.loan_value, loanItem.amount)
                tvStatus.text = loanItem.state.name
                    .lowercase()
                    .replaceFirstChar { it.uppercase() }

                val color = when(loanItem.state) {
                    LoanState.APPROVED -> context.getColor(com.alexs.common.R.color.spring_green)
                    LoanState.REGISTERED -> context.getColor(com.alexs.common.R.color.dodger_blue)
                    LoanState.REJECTED -> context.getColor(com.alexs.common.R.color.tomato)
                }

                ivStatusIcon.setBackgroundColor(color)

                card.setOnClickListener { onLoanClicked(loanItem.loanId) }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LoansListViewHolder(
        ItemLoanBinding.bind(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_loan,
                parent,
                false
            )
        )
    )

    override fun onBindViewHolder(holder: LoansListViewHolder, position: Int) =
        holder.bind(itemPosition = position)

}