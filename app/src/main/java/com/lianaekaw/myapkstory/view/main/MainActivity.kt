package com.lianaekaw.myapkstory.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lianaekaw.myapkstory.R
import com.lianaekaw.myapkstory.ViewModelFactory
import com.lianaekaw.myapkstory.databinding.ActivityMainBinding
import com.lianaekaw.myapkstory.repository.LoadingStateAdapter
import com.lianaekaw.myapkstory.repository.UserAdapter
import com.lianaekaw.myapkstory.view.camera.UploadActivity
import com.lianaekaw.myapkstory.view.maps.MapsActivity
import com.lianaekaw.myapkstory.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var detailAdapter: UserAdapter

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        detailAdapter = UserAdapter()
        setContentView(binding.root)
        getSession()
        setupRecyclerView()

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.logout -> {
                    viewModel.clearUser()
                    viewModel.logoutCompleted.observe(this) { whenLogout ->
                        if (whenLogout) {
                            backLogin()
                        }
                    }
                    true
                }

                R.id.upload -> {
                    val intent = Intent(this, UploadActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.maps -> {
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> {
                    super.onOptionsItemSelected(menuItem)
                }
            }
        }
    }

    private fun backLogin() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun getSession() {
        // Observe user session changes
        viewModel.getUserSession().removeObservers(this) // Remove existing observers to prevent multiple observers issue
        viewModel.getUserSession().observe(this) { user ->
            if (!user.isLogin) {
                val welcomeActivity = Intent(this, WelcomeActivity::class.java)
                welcomeActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(welcomeActivity)
                finish()
            } else {
                // Observe loading state
                if (!viewModel.itsloading.hasObservers()) {
                    viewModel.itsloading.observe(this) { state ->
                        // Handle loading state
                    }
                }
                // Observe stories
                if (!viewModel.story.hasObservers()) {
                    viewModel.seeStories().observe(this) { stories ->
                        // Handle stories
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvHome.layoutManager = LinearLayoutManager(this)
        val loadStateAdapter = LoadingStateAdapter { detailAdapter.retry() }
        binding.rvHome.adapter = detailAdapter.withLoadStateFooter(loadStateAdapter)

        viewModel.story.observe(this) { pagingData ->
            detailAdapter.submitData(lifecycle, pagingData)
        }
    }
}
