package io.github.pavelannin.plantuml.dsl.class_diagram

data class NoteUml(val text: String, val position: Position) {
    enum class Position { Left, Top, Right, Bottom }
}

@UmlMarker
interface NoteAttributeDsl {
    fun left(text: String)
    fun right(text: String)
}

@UmlMarker
interface NoteDeclaringElementDsl {
    fun left(text: String)
    fun top(text: String)
    fun right(text: String)
    fun bottom(text: String)
}

class NotesBuilder : NoteAttributeDsl, NoteDeclaringElementDsl {
    private val _notes = mutableListOf<NoteUml>()
    val notes: List<NoteUml> get() = _notes.toList()

    override fun left(text: String) {
        _notes.add(NoteUml(text = text, position = NoteUml.Position.Left))
    }

    override fun top(text: String) {
        _notes.add(NoteUml(text = text, position = NoteUml.Position.Top))
    }

    override fun right(text: String) {
        _notes.add(NoteUml(text = text, position = NoteUml.Position.Right))
    }

    override fun bottom(text: String) {
        _notes.add(NoteUml(text = text, position = NoteUml.Position.Bottom))
    }
}
