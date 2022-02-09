package com.bb.crudkotlin

import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.bb.crudkotlin.adapter.DBAdapter
import kotlinx.android.synthetic.main.activity_add_data.*
import java.lang.Exception

class AddDataActivity : AppCompatActivity() {

    var id=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_data)

        try {
            var bundle: Bundle = intent.extras!!
            id = bundle.getInt("MainActId", 0)
            if (id !=0){
                addName.setText(bundle.getString("MainActName"))
                addPhone.setText(bundle.getString("MainActPhone"))
                addEmail.setText(bundle.getString("MainActEmail"))
                addAlamat.setText(bundle.getString("MainActAlamat"))
            }
        }catch (ex: Exception){
        }
        
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.add_menu, menu)

        val itemDelete: MenuItem = menu.findItem(R.id.action_delete)
        if (id == 0) {
            itemDelete.isVisible = false
        }else{
            itemDelete.isVisible = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_save-> {
                var dbAdapter = DBAdapter(this)

                var values = ContentValues()
                values.put("name", addName.text.toString())
                values.put("phone", addPhone.text.toString())
                values.put("email", addEmail.text.toString())
                values.put("alamat", addAlamat.text.toString())

                if (id == 0){
                    val mID = dbAdapter.insert(values)

                    if (mID > 0){
                        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    var selectionArgs = arrayOf(id.toString())
                    val mID = dbAdapter.update(values, "Id=?", selectionArgs)
                    if (mID > 0){
                        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        Toast.makeText(this, "Failed to update data", Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }
            R.id.action_delete ->{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Delete Data")
                builder.setMessage("This Data Will Be Deleted")

                builder.setPositiveButton("Delete") {dialog: DialogInterface?, which: Int ->
                    var dbAdapter = DBAdapter(this)
                    val selectionArgs = arrayOf(id.toString())
                    dbAdapter.delete("Id=?", selectionArgs)
                    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
                    finish()
                }
                builder.setNegativeButton("Cancel"){dialog: DialogInterface?, which: Int ->  }

                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}