package com.anilkumawat3104.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class notes : AppCompatActivity() {
    private lateinit var addButton : FloatingActionButton
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager
   private lateinit var adapter : FirestoreRecyclerAdapter<firebasemodel,noteViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)
        addButton = findViewById(R.id.additem)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser!!
        firebaseFirestore = FirebaseFirestore.getInstance()
        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.setHasFixedSize(true)
        staggeredGridLayoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager

        var query : Query
        query = firebaseFirestore.collection("notes").document(firebaseUser.uid).collection("myNotes").orderBy("title",Query.Direction.ASCENDING)


        var allnotes : FirestoreRecyclerOptions<firebasemodel>
        allnotes = FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query,firebasemodel::class.java).build()

       adapter = object : FirestoreRecyclerAdapter<firebasemodel, noteViewHolder>(allnotes) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): noteViewHolder {
                return noteViewHolder(
                    LayoutInflater.from(parent.context)
                    .inflate(R.layout.noteslayout, parent, false))
            }

           override fun onBindViewHolder(holder: noteViewHolder, position: Int, model: firebasemodel) {

               var doc : String = adapter.snapshots.getSnapshot(position).id
               var ref : DocumentReference= firebaseFirestore.collection("notes").document(firebaseUser.uid).collection("myNotes").document(doc)


               holder.bind(model,ref)
            }

            override fun onDataChanged() {
                // Called each time there is a new data snapshot. You may want to use this method
                // to hide a loading spinner or check for the "no documents" state and update your UI.
                // ...
            }
        }
        recyclerView.adapter = adapter
        addButton.setOnClickListener{
            val intent = Intent(this,noteActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        if(adapter!=null)
        adapter.startListening()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.logout){
            firebaseAuth.signOut()
            finish()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}
class noteViewHolder(val itemView: View, var model: firebasemodel? = null) : RecyclerView.ViewHolder(itemView) {

    lateinit var notetile : TextView
    lateinit var norecontent : TextView
    lateinit var image : ImageView
    fun bind(model: firebasemodel,pos : DocumentReference) {
        var layout : LinearLayout
        val code = randomcolor()
        layout = itemView.findViewById(R.id.note)
        layout.setBackgroundColor(itemView.resources.getColor(code))
        notetile = itemView.findViewById(R.id.titlenote)
        norecontent = itemView.findViewById(R.id.notecontant)
        notetile.text = model.getTitle()
        norecontent.text = model.getContent()
        image = itemView.findViewById(R.id.popbutton)

        image.setOnClickListener {
            // Initializing the popup menu and giving the reference as current context
            val popupMenu = PopupMenu(itemView.context, image)

            // Inflating popup menu from popup_menu.xml file
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                // Toast message on menu item clicked
                if(menuItem.itemId==R.id.Delete){
                 pos.delete().addOnSuccessListener {
                  Toast.makeText(itemView.context,"deleted",Toast.LENGTH_SHORT).show()
                 }
                 }
                true
            }
            // Showing the popup menu
            popupMenu.show()
        }
    }

    private fun randomcolor(): Int {
        val code = ArrayList<Int>()
        code.add(R.color.color1)
        code.add(R.color.color2)
        code.add(R.color.color3)
        code.add(R.color.color4)
        code.add(R.color.pink)
        code.add(R.color.gray)
        code.add(R.color.green)
        code.add(R.color.lightgreen)
        code.add(R.color.skyblue)
        return code.get((0..code.size).random())
    }

}