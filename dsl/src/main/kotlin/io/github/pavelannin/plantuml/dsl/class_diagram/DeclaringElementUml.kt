package io.github.pavelannin.plantuml.dsl.class_diagram

sealed class DeclaringElementUml(
    open val name: String,
    open val notes: List<NoteUml>,
) {

    data class AbstractClass(
        override val name: String,
        override val notes: List<NoteUml>,
        val attributes: List<AttributeUml>,
    ) : DeclaringElementUml(name, notes)

    data class Annotation(
        override val name: String,
        override val notes: List<NoteUml>,
        val attributes: List<AttributeUml>,
    ) : DeclaringElementUml(name, notes)

    data class Circle(
        override val name: String,
        override val notes: List<NoteUml>,
    ) : DeclaringElementUml(name, notes)

    data class Class(
        override val name: String,
        override val notes: List<NoteUml>,
        val attributes: List<AttributeUml>,
    ) : DeclaringElementUml(name, notes)

    data class Diamond(
        override val name: String,
        override val notes: List<NoteUml>,
    ) : DeclaringElementUml(name, notes)

    data class Entity(
        override val name: String,
        override val notes: List<NoteUml>,
        val attributes: List<AttributeUml>,
    ) : DeclaringElementUml(name, notes)

    data class Enum(
        override val name: String,
        override val notes: List<NoteUml>,
        val attributes: List<AttributeUml>,
    ) : DeclaringElementUml(name, notes)

    data class Exception(
        override val name: String,
        override val notes: List<NoteUml>,
        val attributes: List<AttributeUml>,
    ) : DeclaringElementUml(name, notes)

    data class Interface(
        override val name: String,
        override val notes: List<NoteUml>,
        val attributes: List<AttributeUml>,
    ) : DeclaringElementUml(name, notes)

    data class MetaClass(
        override val name: String,
        override val notes: List<NoteUml>,
        val attributes: List<AttributeUml>,
    ) : DeclaringElementUml(name, notes)

    data class Protocol(
        override val name: String,
        override val notes: List<NoteUml>,
        val attributes: List<AttributeUml>,
    ) : DeclaringElementUml(name, notes)

    data class Stereotype(
        override val name: String,
        override val notes: List<NoteUml>,
        val attributes: List<AttributeUml>,
    ) : DeclaringElementUml(name, notes)

    data class Struct(
        override val name: String,
        override val notes: List<NoteUml>,
        val attributes: List<AttributeUml>,
    ) : DeclaringElementUml(name, notes)
}

@UmlMarker
interface AnyBasicDeclaringElementScopeDsl {
    fun notes(init: NoteDeclaringElementDsl.() -> Unit)
}

class AnyBasicDeclaringElementScopeBuilder(
    val notesBuilder: NotesBuilder = NotesBuilder(),
) : AnyBasicDeclaringElementScopeDsl {

    override fun notes(init: NoteDeclaringElementDsl.() -> Unit) {
        notesBuilder.apply(init)
    }
}

@UmlMarker
interface AnyClassDeclaringElementScopeDsl : AnyBasicDeclaringElementScopeDsl, PropertyDsl, MethodDsl

class AnyClassDeclaringElementScopeBuilder(
    val anyBasicBuilder: AnyBasicDeclaringElementScopeBuilder = AnyBasicDeclaringElementScopeBuilder(),
    val attributeBuilder: AttributeBuilder = AttributeBuilder(),
) : AnyClassDeclaringElementScopeDsl,
    AnyBasicDeclaringElementScopeDsl by anyBasicBuilder,
    PropertyDsl by attributeBuilder,
    MethodDsl by attributeBuilder

@UmlMarker
interface AnyEnumDeclaringElementScopeDsl : AnyBasicDeclaringElementScopeDsl, EnumCaseDsl, MethodDsl

class AnyEnumDeclaringElementScopeBuilder(
    val anyBasicBuilder: AnyBasicDeclaringElementScopeBuilder = AnyBasicDeclaringElementScopeBuilder(),
    val attributeBuilder: AttributeBuilder = AttributeBuilder(),
) : AnyEnumDeclaringElementScopeDsl,
    AnyBasicDeclaringElementScopeDsl by anyBasicBuilder,
    EnumCaseDsl by attributeBuilder,
    MethodDsl by attributeBuilder

@UmlMarker
interface DeclaringElementDsl {
    fun abstractClass(name: String, scope: AnyClassDeclaringElementScopeDsl.() -> Unit = {}): DeclaringElementUml.AbstractClass
    fun annotation(name: String, scope: AnyClassDeclaringElementScopeDsl.() -> Unit = {}): DeclaringElementUml.Annotation
    fun circle(name: String, scope: AnyBasicDeclaringElementScopeDsl.() -> Unit = {}): DeclaringElementUml.Circle
    fun `class`(name: String, scope: AnyClassDeclaringElementScopeDsl.() -> Unit = {}): DeclaringElementUml.Class
    fun diamond(name: String, scope: AnyBasicDeclaringElementScopeDsl.() -> Unit = {}): DeclaringElementUml.Diamond
    fun entity(name: String, scope: AnyClassDeclaringElementScopeDsl.() -> Unit = {}): DeclaringElementUml.Entity
    fun enum(name: String, scope: AnyEnumDeclaringElementScopeDsl.() -> Unit = {}): DeclaringElementUml.Enum
    fun exception(name: String, scope: AnyClassDeclaringElementScopeDsl.() -> Unit = {}): DeclaringElementUml.Exception
    fun `interface`(name: String, scope: AnyClassDeclaringElementScopeDsl.() -> Unit = {}): DeclaringElementUml.Interface
    fun metaClass(name: String, scope: AnyClassDeclaringElementScopeDsl.() -> Unit = {}): DeclaringElementUml.MetaClass
    fun protocol(name: String, scope: AnyClassDeclaringElementScopeDsl.() -> Unit = {}): DeclaringElementUml.Protocol
    fun stereotype(name: String, scope: AnyClassDeclaringElementScopeDsl.() -> Unit = {}): DeclaringElementUml.Stereotype
    fun struct(name: String, scope: AnyClassDeclaringElementScopeDsl.() -> Unit ={}): DeclaringElementUml.Struct
}

class DeclaringElementBuilder : DeclaringElementDsl {
    private val _elements = mutableListOf<DeclaringElementUml>()
    val elements: List<DeclaringElementUml> get() = _elements.toList()

    override fun abstractClass(name: String, scope: AnyClassDeclaringElementScopeDsl.() -> Unit): DeclaringElementUml.AbstractClass {
        val scopeBuilder = AnyClassDeclaringElementScopeBuilder().apply(scope)
        return DeclaringElementUml.AbstractClass(
            name = name,
            attributes = scopeBuilder.attributeBuilder.attributes,
            notes = scopeBuilder.anyBasicBuilder.notesBuilder.notes
        ).apply(_elements::add)
    }

    override fun annotation(name: String, scope: AnyClassDeclaringElementScopeDsl.() -> Unit): DeclaringElementUml.Annotation {
        val scopeBuilder = AnyClassDeclaringElementScopeBuilder().apply(scope)
        return DeclaringElementUml.Annotation(
            name = name,
            attributes = scopeBuilder.attributeBuilder.attributes,
            notes = scopeBuilder.anyBasicBuilder.notesBuilder.notes
        ).apply(_elements::add)
    }

    override fun circle(name: String, scope: AnyBasicDeclaringElementScopeDsl.() -> Unit): DeclaringElementUml.Circle {
        val scopeBuilder = AnyBasicDeclaringElementScopeBuilder().apply(scope)
        return DeclaringElementUml.Circle(
            name = name,
            notes = scopeBuilder.notesBuilder.notes
        ).apply(_elements::add)
    }

    override fun `class`(name: String, scope: AnyClassDeclaringElementScopeDsl.() -> Unit): DeclaringElementUml.Class {
        val scopeBuilder = AnyClassDeclaringElementScopeBuilder().apply(scope)
        return DeclaringElementUml.Class(
            name = name,
            attributes = scopeBuilder.attributeBuilder.attributes,
            notes = scopeBuilder.anyBasicBuilder.notesBuilder.notes
        ).apply(_elements::add)
    }

    override fun diamond(name: String, scope: AnyBasicDeclaringElementScopeDsl.() -> Unit): DeclaringElementUml.Diamond {
        val scopeBuilder = AnyBasicDeclaringElementScopeBuilder().apply(scope)
        return DeclaringElementUml.Diamond(
            name = name,
            notes = scopeBuilder.notesBuilder.notes
        ).apply(_elements::add)
    }

    override fun entity(name: String, scope: AnyClassDeclaringElementScopeDsl.() -> Unit): DeclaringElementUml.Entity {
        val scopeBuilder = AnyClassDeclaringElementScopeBuilder().apply(scope)
        return DeclaringElementUml.Entity(
            name = name,
            attributes = scopeBuilder.attributeBuilder.attributes,
            notes = scopeBuilder.anyBasicBuilder.notesBuilder.notes
        ).apply(_elements::add)
    }

    override fun enum(name: String, scope: AnyEnumDeclaringElementScopeDsl.() -> Unit): DeclaringElementUml.Enum {
        val scopeBuilder = AnyEnumDeclaringElementScopeBuilder().apply(scope)
        return DeclaringElementUml.Enum(
            name = name,
            attributes = scopeBuilder.attributeBuilder.attributes,
            notes = scopeBuilder.anyBasicBuilder.notesBuilder.notes
        ).apply(_elements::add)
    }

    override fun exception(name: String, scope: AnyClassDeclaringElementScopeDsl.() -> Unit): DeclaringElementUml.Exception {
        val scopeBuilder = AnyClassDeclaringElementScopeBuilder().apply(scope)
        return DeclaringElementUml.Exception(
            name = name,
            attributes = scopeBuilder.attributeBuilder.attributes,
            notes = scopeBuilder.anyBasicBuilder.notesBuilder.notes
        ).apply(_elements::add)
    }

    override fun `interface`(name: String, scope: AnyClassDeclaringElementScopeDsl.() -> Unit): DeclaringElementUml.Interface {
        val scopeBuilder = AnyClassDeclaringElementScopeBuilder().apply(scope)
        return DeclaringElementUml.Interface(
            name = name,
            attributes = scopeBuilder.attributeBuilder.attributes,
            notes = scopeBuilder.anyBasicBuilder.notesBuilder.notes
        ).apply(_elements::add)
    }

    override fun metaClass(name: String, scope: AnyClassDeclaringElementScopeDsl.() -> Unit): DeclaringElementUml.MetaClass {
        val scopeBuilder = AnyClassDeclaringElementScopeBuilder().apply(scope)
        return DeclaringElementUml.MetaClass(
            name = name,
            attributes = scopeBuilder.attributeBuilder.attributes,
            notes = scopeBuilder.anyBasicBuilder.notesBuilder.notes
        ).apply(_elements::add)
    }

    override fun protocol(name: String, scope: AnyClassDeclaringElementScopeDsl.() -> Unit): DeclaringElementUml.Protocol {
        val scopeBuilder = AnyClassDeclaringElementScopeBuilder().apply(scope)
        return DeclaringElementUml.Protocol(
            name = name,
            attributes = scopeBuilder.attributeBuilder.attributes,
            notes = scopeBuilder.anyBasicBuilder.notesBuilder.notes
        ).apply(_elements::add)
    }

    override fun stereotype(name: String, scope: AnyClassDeclaringElementScopeDsl.() -> Unit): DeclaringElementUml.Stereotype {
        val scopeBuilder = AnyClassDeclaringElementScopeBuilder().apply(scope)
        return DeclaringElementUml.Stereotype(
            name = name,
            attributes = scopeBuilder.attributeBuilder.attributes,
            notes = scopeBuilder.anyBasicBuilder.notesBuilder.notes
        ).apply(_elements::add)
    }

    override fun struct(name: String, scope: AnyClassDeclaringElementScopeDsl.() -> Unit): DeclaringElementUml.Struct {
        val scopeBuilder = AnyClassDeclaringElementScopeBuilder().apply(scope)
        return DeclaringElementUml.Struct(
            name = name,
            attributes = scopeBuilder.attributeBuilder.attributes,
            notes = scopeBuilder.anyBasicBuilder.notesBuilder.notes
        ).apply(_elements::add)
    }
}
