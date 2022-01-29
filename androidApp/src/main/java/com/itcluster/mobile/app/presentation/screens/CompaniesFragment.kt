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
import com.itcluster.mobile.app.presentation.view.CompaniesRvAdapter
import com.itcluster.mobile.app.presentation.view.MainActivity
import com.itcluster.mobile.app.presentation.view.loading.LoadingView
import com.itcluster.mobile.feature.list.di.CompaniesFactory
import com.itcluster.mobile.feature.list.model.state.LoginState
import com.itcluster.mobile.feature.list.presentation.CompaniesVm
import com.itcluster.mobile.presentation.models.CompanyModel
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

    private val loadingView: LoadingView by lazy { requireView().findViewById(R.id.loading) }

    @Inject
    lateinit var factory: CompaniesFactory

//    private val adapter by lazy { createAdapter() }

    private val companiesRvAdapter = CompaniesRvAdapter(listOf())

    override fun viewModelFactory(): ViewModelProvider.Factory =
        createViewModelFactory { factory.createCompaniesViewModel() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingView.isVisible = false
        viewModel.state.addCloseableObserver { companiesState ->
            when (companiesState) {
                is LoginState.Companies.Auth -> {
                    loadingView.isVisible = false
                    (activity as MainActivity).navController.navigate(R.id.action_to_main)
                }
                is LoginState.Companies.Data -> {
                    loadingView.isVisible = false
                    fillDataAdapter(companiesState.companies)
                }
                is LoginState.Error -> {
                    loadingView.isVisible = false
                    when (companiesState) {
                        is LoginState.Error.Unknown -> showToast(companiesState.message)
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
        with(binding) {
            companies.adapter = companiesRvAdapter
            companies.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun createAdapter() {
//        viewModel.authTokenRequest()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fillDataAdapter(companies: List<CompanyModel>) {
        companiesRvAdapter.companies = companies
        companiesRvAdapter.notifyDataSetChanged()
    }

    private fun showToast(message: String?) {
        message?.also {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }
    }

    companion object {


    }
}