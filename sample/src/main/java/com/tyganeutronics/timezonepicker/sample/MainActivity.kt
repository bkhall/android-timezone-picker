package com.tyganeutronics.timezonepicker.sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.android.timezonepicker.TimeZoneInfo
import com.android.timezonepicker.TimeZonePickerDialog
import com.tyganeutronics.timezonepicker.TimeZonePickerBottomSheetFragment
import com.tyganeutronics.timezonepicker.TimeZonePickerDialogFragment
import com.tyganeutronics.timezonepicker.TimeZonePickerFragment
import java.util.TimeZone

class MainActivity : AppCompatActivity(), View.OnClickListener,
    TimeZonePickerDialog.OnTimeZoneSetListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        applyWindowInsets()

        findViewById<Button>(R.id.btn_bottomsheet_picker).setOnClickListener(this)
        findViewById<Button>(R.id.btn_dialog_picker).setOnClickListener(this)
        findViewById<Button>(R.id.btn_privacy_policy).setOnClickListener(this)

        val fragment = TimeZonePickerFragment.newInstance(this)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }

    override fun onClick(view: View?) {
        if (view == null) {
            return
        }

        when (view.id) {
            R.id.btn_bottomsheet_picker -> {

                val timeZonePickerDialog = TimeZonePickerBottomSheetFragment.newInstance(
                    System.currentTimeMillis(),
                    TimeZone.getDefault(),
                    this
                )
                timeZonePickerDialog.show(
                    supportFragmentManager,
                    TimeZonePickerBottomSheetFragment.TAG
                )
            }

            R.id.btn_dialog_picker -> {

                val timeZonePickerDialog = TimeZonePickerDialogFragment.newInstance(
                    System.currentTimeMillis(),
                    TimeZone.getDefault(),
                    this
                )
                timeZonePickerDialog.show(
                    supportFragmentManager,
                    TimeZonePickerDialogFragment.TAG
                )
            }

            R.id.btn_privacy_policy -> {
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.data = getString(R.string.privacy_policy_url).toUri()

                startActivity(intent)
            }
        }
    }

    override fun onTimeZoneSet(tzi: TimeZoneInfo?) {
        if (tzi == null) {
            return
        }

        findViewById<View>(R.id.results_container).run {
            findViewById<TextView>(com.tyganeutronics.timezonepicker.R.id.time_zone).text =
                tzi.displayName

            findViewById<TextView>(com.tyganeutronics.timezonepicker.R.id.time_offset).text =
                tzi.getGmtDisplayName(this@MainActivity)

            findViewById<TextView>(com.tyganeutronics.timezonepicker.R.id.location).apply {
                val location = tzi.country
                if (location == null) {
                    visibility = View.INVISIBLE
                } else {
                    text = location
                    visibility = View.VISIBLE
                }
            }
        }
    }

    private fun applyWindowInsets() {
        // https://developer.android.com/develop/ui/views/layout/edge-to-edge#kotlin
        val container = findViewById<CoordinatorLayout>(R.id.layout_container);

        ViewCompat.setOnApplyWindowInsetsListener(container) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = insets.left
                topMargin = insets.top
                bottomMargin = insets.bottom
                rightMargin = insets.right
            }

            WindowInsetsCompat.CONSUMED
        }
    }
}
