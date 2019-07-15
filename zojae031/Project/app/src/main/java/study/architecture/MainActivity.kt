package study.architecture

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import study.architecture.adapter.MainPageAdapter
import study.architecture.model.DataParser

class MainActivity : AppCompatActivity() {
    init {
        DataParser
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tabSetting()
        pagerSetting()
    }

    private fun tabSetting() {
        with(tabLayout) {
            tabGravity = TabLayout.GRAVITY_FILL
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    pager.currentItem = tab!!.position
                }
            })
        }
    }

    private fun pagerSetting() {
        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        pager.adapter = MainPageAdapter(supportFragmentManager)

    }

    companion object {
        const val TAB_NUMBER = 4
    }


}
