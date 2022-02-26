package com.itcluster.mobile.app.presentation.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.itcluster.mobile.app.BR
import com.itcluster.mobile.app.R
import com.itcluster.mobile.app.databinding.FragmentCompaniesBinding
import com.itcluster.mobile.app.ext.log.LogSniffer
import com.itcluster.mobile.app.ext.recycler.BaseDelegationAdapter
import com.itcluster.mobile.app.models.CompanyState
import com.itcluster.mobile.app.models.CompanyState.Companion.toState
import com.itcluster.mobile.app.presentation.view.MainActivity
import com.itcluster.mobile.app.presentation.view.adapters.CompanyAdapterDelegates
import com.itcluster.mobile.feature.list.di.CompaniesFactory
import com.itcluster.mobile.feature.list.model.state.LoginState.*
import com.itcluster.mobile.feature.list.presentation.CompaniesVm
import dagger.hilt.android.AndroidEntryPoint
import dev.icerock.moko.mvvm.MvvmFragment
import dev.icerock.moko.mvvm.createViewModelFactory
import dev.icerock.moko.mvvm.livedata.addCloseableObserver
import javax.inject.Inject

@AndroidEntryPoint
class CompaniesFragment : MvvmFragment<FragmentCompaniesBinding, CompaniesVm>() {

    override val layoutId: Int = R.layout.fragment_companies
    override val viewModelVariableId: Int = BR.viewModel
    override val viewModelClass = CompaniesVm::class.java

    @Inject
    lateinit var factory: CompaniesFactory

    private val adapter by lazy { createAdapter() }

    override fun viewModelFactory(): ViewModelProvider.Factory =
        createViewModelFactory { factory.createCompaniesViewModel() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loading.isVisible = false
        viewModel.state.addCloseableObserver { companiesState ->
            when (companiesState) {
                is Companies.Auth -> {
                    binding.loading.isVisible = false
                    (activity as MainActivity).navController.navigate(R.id.action_to_wallets)
                }
                is Companies.Data -> {
                    binding.loading.isVisible = false
                    fillDataAdapter(companiesState.companies.map { it.toState() })
                }
                is Error -> {
                    binding.loading.isVisible = false
                    when (companiesState) {
                        is Error.Unknown -> {
                            LogSniffer.addLog(TAG + companiesState.throwable.toString())
                            showToast(companiesState.message)
                        }
                        else -> {

                        }
                    }

                }
                else -> {}
            }
        }

        setupViews()
    }

    private fun setupViews() {
        binding.companies.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@CompaniesFragment.adapter
        }
    }

    private fun createAdapter() = BaseDelegationAdapter(
        CompanyAdapterDelegates.companyAdapterDelegate(
            ::navToWalletsPage
        )
    )

    private fun navToWalletsPage(companyId: Long) {
        binding.loading.isVisible = true
        viewModel.authTokenRequest(companyId)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fillDataAdapter(companies: List<CompanyState>) {
        adapter.items = companies
        adapter.notifyDataSetChanged()
    }

    private fun showToast(message: String?) {
        message?.also {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }
    }

    companion object {

        private const val TAG = "CompaniesFragment: "
    }
}