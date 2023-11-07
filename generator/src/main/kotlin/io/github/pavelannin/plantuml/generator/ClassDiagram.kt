package io.github.pavelannin.plantuml.generator

import io.github.pavelannin.plantuml.dsl.class_diagram.*

internal fun convertClassDiagramToPlantuml(uml: ClassDiagramUml): String = buildString {
    appendLine("@startuml")
    appendLine()

    uml.elements
        .map(DeclaringElementUml::convertToPlantuml)
        .onEach(::append)

    uml.relations
        .map(RelationsUml::convertToPlantuml)
        .onEach(::appendLine)

    appendLine()
    appendLine("@enduml")
}

private fun DeclaringElementUml.convertToPlantuml(): String {
    fun DeclaringElementUml.declaration(): String = when (this) {
        is DeclaringElementUml.AbstractClass -> "abstract class"
        is DeclaringElementUml.Annotation -> "annotation"
        is DeclaringElementUml.Circle -> "circle"
        is DeclaringElementUml.Class -> "class"
        is DeclaringElementUml.Diamond -> "diamond"
        is DeclaringElementUml.Entity -> "entity"
        is DeclaringElementUml.Enum -> "enum"
        is DeclaringElementUml.Exception -> "exception"
        is DeclaringElementUml.Interface -> "interface"
        is DeclaringElementUml.MetaClass -> "metaclass"
        is DeclaringElementUml.Protocol -> "protocol"
        is DeclaringElementUml.Stereotype -> "stereotype"
        is DeclaringElementUml.Struct -> "struct"
    }

    fun DeclaringElementUml.attributes(): List<AttributeUml>? = when (val element = this) {
        is DeclaringElementUml.AbstractClass -> element.attributes
        is DeclaringElementUml.Annotation -> element.attributes
        is DeclaringElementUml.Class -> element.attributes
        is DeclaringElementUml.Entity -> element.attributes
        is DeclaringElementUml.Enum -> element.attributes
        is DeclaringElementUml.Exception -> element.attributes
        is DeclaringElementUml.Interface -> element.attributes
        is DeclaringElementUml.MetaClass -> element.attributes
        is DeclaringElementUml.Protocol -> element.attributes
        is DeclaringElementUml.Stereotype -> element.attributes
        is DeclaringElementUml.Struct -> element.attributes
        is DeclaringElementUml.Circle, is DeclaringElementUml.Diamond -> null

    }

    return buildString {
        attributes()?.takeIf(List<AttributeUml>::isNotEmpty)?.let { attributes ->
            appendLine("${declaration()} $name {")
            attributes.map(AttributeUml::convertToPlantuml).onEach(::appendLine)
            appendLine("}")
        } ?: appendLine("${declaration()} $name")

        appendLine()
        notes.asSequence()
            .map { it.convertToPlantuml(name) }
            .onEach(::appendLine)
            .forEach { _ -> appendLine() }
        attributes()
            ?.asSequence()
            ?.flatMap { attribute -> attribute.notes.map { attribute.name to it } }
            ?.map { (name, note) -> note.convertToPlantuml("${this@convertToPlantuml.name}::$name") }
            ?.onEach(::appendLine)
            ?.forEach { _ -> appendLine() }
    }
}

private fun AttributeUml.convertToPlantuml(): String {
    fun AttributeUml.declaration(): String = when (val attribute = this) {
        is AttributeUml.Property ->
            buildString {
                append("{field} ${attribute.name}")
                if (attribute.type.isNotBlank()) append(": ${attribute.type}")
            }

        is AttributeUml.Method ->
            buildString {
                append("{method} ${attribute.name}")
                append("(")
                attribute.arguments
                    .joinToString(separator = ", ") { "${it.name}${if (it.type.isNotBlank()) ": ${it.type}" else ""}" }
                    .let(::append)
                append(")")
                attribute.returnType?.let { append(": $it") }
            }

        is AttributeUml.EnumCase ->
            buildString {
                append("{field} ${attribute.name}")
                if (attribute.arguments.isNotEmpty()) {
                    append("(")
                    attribute.arguments
                        .joinToString(separator = ", ") { "${it.name}${if (it.type.isNotBlank()) ": ${it.type}" else ""}" }
                        .let(::append)
                    append(")")
                }
            }
    }

    fun AccessControlUml.Type.declaration(): String = when (this) {
        AccessControlUml.Type.Public -> "+"
        AccessControlUml.Type.Private -> "-"
        AccessControlUml.Type.PrivatePackage -> "~"
        AccessControlUml.Type.Protected -> "#"
    }

    fun ModifierUml.declaration(): String? = listOfNotNull(
        if (isStatic) "{static}" else null,
        if (isAbstract) "{abstract}" else null,
    ).takeIf(List<String>::isNotEmpty)?.joinToString(separator = " ")

    return listOfNotNull(
        this.accessControl?.type?.declaration(),
        this.modifier.declaration(),
        this.declaration()
    ).joinToString(separator = " ")
}

private fun NoteUml.convertToPlantuml(linkName: String): String {
    fun NoteUml.Position.declaration(): String = when (this) {
        NoteUml.Position.Left -> "left"
        NoteUml.Position.Top -> "top"
        NoteUml.Position.Right -> "right"
        NoteUml.Position.Bottom -> "bottom"
    }
    return buildString {
        appendLine("note ${position.declaration()} of $linkName")
        appendLine(text)
        append("end note")
    }
}

private fun RelationsUml.convertToPlantuml(): String {
    fun RelationsUml.Type.declaration(): String = when (this) {
        RelationsUml.Type.Extension -> "<|--"
        RelationsUml.Type.Composition -> "*--"
        RelationsUml.Type.Aggregation -> "o--"
        RelationsUml.Type.SolidLine -> "--"
        RelationsUml.Type.DashedLine -> ".."
        RelationsUml.Type.DirectedSolidLine -> "-->"
        RelationsUml.Type.DirectedDashedLine -> "..>"
    }
    return "${from.name} ${type.declaration()} ${to.name}"
}
