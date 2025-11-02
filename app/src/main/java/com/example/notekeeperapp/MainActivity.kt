package com.example.notekeeperapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.ArrayList

public final class MainActivity : AppCompatActivity(), NoteAdapter.OnNoteListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    private val noteList = ArrayList<Note>()
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        val fab: FloatingActionButton = findViewById(R.id.fab)

        db = DatabaseHelper(this)

        noteAdapter = NoteAdapter(noteList, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = noteAdapter

        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadNotes() {
        noteList.clear()
        noteList.addAll(db.getAllNotes())
        noteAdapter.notifyDataSetChanged()

        if (noteList.isEmpty()) {
            Toast.makeText(this, "No notes yet. Add one!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        loadNotes()
    }

    override fun onNoteClick(position: Int) {
        val note = noteList[position]
        val intent = Intent(this, AddEditNoteActivity::class.java).apply {
            putExtra("NOTE_ID", note.id)
        }
        startActivity(intent)
    }

    override fun onNoteLongClick(position: Int) {
        val note = noteList[position]
        AlertDialog.Builder(this)
            .setTitle("Delete Note")
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Yes") { _, _ ->
                db.deleteNote(note.id)
                loadNotes()
            }
            .setNegativeButton("No", null)
            .show()
    }
}
