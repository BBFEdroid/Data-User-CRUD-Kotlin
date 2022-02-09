package com.bb.crudkotlin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import com.bb.crudkotlin.adapter.DBAdapter
import kotlinx.android.synthetic.main.activity_data_list.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var listData = ArrayList<DataUser>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener{ view ->
            var intent = Intent(this,AddDataActivity::class.java)
            startActivity(intent)
        }
        loadData()
    }
    override fun onResume() {
        super.onResume()
        loadData()

    }

    @SuppressLint("Range")
    private fun loadData() {
        var dbAdapter = DBAdapter(this)
        var cursor = dbAdapter.allQuery()

        listData.clear()
        if (cursor.moveToFirst()){
            do {
                val user_id = cursor.getInt(cursor.getColumnIndex("id"))
                val user_name = cursor.getString(cursor.getColumnIndex("name"))
                val user_phone = cursor.getString(cursor.getColumnIndex("phone"))
                val user_email = cursor.getString(cursor.getColumnIndex("email"))
                val user_alamat = cursor.getString(cursor.getColumnIndex("alamat"))

                listData.add(DataUser(user_id, user_name, user_phone, user_email, user_alamat))
            }while (cursor.moveToNext())
        }

        var dataAdapter = DataAdapter(this, listData)
        rvData.adapter = dataAdapter
    }

    inner class DataAdapter: BaseAdapter{

        private var dataList = ArrayList<DataUser>()
        private var context: Context? = null

        constructor(context: Context, dataList: ArrayList<DataUser>) : super(){
            this.dataList = dataList
            this.context = context
        }

        override fun getCount(): Int {
            return dataList.size
        }

        override fun getItem(position: Int): Any {
            return dataList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view: View?
            val vh: ViewHolder

            if (convertView == null){
                view = layoutInflater.inflate(R.layout.layout_item_data_list_users, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
                Log.i("db", "set tag for ViewHolder, position: " + position)
            }else{
                view = convertView
                vh = view.tag as ViewHolder
            }

            var mDataUser = dataList[position]
            vh.itemUserName.text = mDataUser.user_name
            vh.itemUserPhone.text = mDataUser.user_phone
            vh.itemUserEmail.text = mDataUser.user_email
            vh.itemUserAlamat.text = mDataUser.user_alamat

            rvData.onItemClickListener = AdapterView.OnItemClickListener{ adapterView, view, position, id ->
                updateData(mDataUser)
            }
            return view
        }

        private fun updateData(dataUser: DataUser) {
            var intent = Intent(this@MainActivity, AddDataActivity::class.java)
            intent.putExtra("MainActId", dataUser.user_id)
            intent.putExtra("MainActName", dataUser.user_name)
            intent.putExtra("MainActPhone", dataUser.user_phone)
            intent.putExtra("MainActEmail", dataUser.user_email)
            intent.putExtra("MainActAlamat", dataUser.user_alamat)
            startActivity(intent)

        }
    }

    private class  ViewHolder(view: View?){
        val itemUserName: TextView
        val itemUserPhone: TextView
        val itemUserEmail: TextView
        val itemUserAlamat: TextView

        init {
            this.itemUserName = view?.findViewById(R.id.item_user_name) as TextView
            this.itemUserPhone = view?.findViewById(R.id.item_user_phone) as TextView
            this.itemUserEmail = view?.findViewById(R.id.item_user_email) as TextView
            this.itemUserAlamat = view?.findViewById(R.id.item_user_alamat) as TextView
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

}