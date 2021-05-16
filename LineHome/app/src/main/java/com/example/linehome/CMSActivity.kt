    package com.example.linehome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_c_m_s.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.btnOnBack

    class CMSActivity : AppCompatActivity() {

    val categories = arrayOf("Estudio","Piso","Dúplex","Ático","Bajo","Loft", "Departamento")
    var category: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c_m_s)

        btnOnBack.setOnClickListener{
            showHome()
        }

        spinner3.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,categories)

        spinner3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                category = categories.get(0)
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                category = categories.get(p2)
            }

        }

    }

    private fun showHome() {
        val activityHome = Intent(this, HomeActivity::class.java)
        startActivity(activityHome)
        finish()
    }
}