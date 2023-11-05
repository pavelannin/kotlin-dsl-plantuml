package io.github.pavelannin.plantuml.dsl.class_diagram

sealed class AttributeUml(
    open val name: String,
    open val notes: List<NoteUml>,
    open val modifier: ModifierUml,
    open val accessControl: AccessControlUml?,
) {
    data class Property(
        override val name: String,
        val type: String,
        override val notes: List<NoteUml>,
        override val modifier: ModifierUml,
        override val accessControl: AccessControlUml?,
    ) : AttributeUml(name, notes, modifier, accessControl)

    data class Method(
        override val name: String,
        val returnType: String?,
        val arguments: List<Argument>,
        override val notes: List<NoteUml>,
        override val modifier: ModifierUml,
        override val accessControl: AccessControlUml?,
    ) : AttributeUml(name, notes, modifier, accessControl) {
        data class Argument(val name: String, val type: String)
    }

    data class EnumCase(
        override val name: String,
        val arguments: List<Argument>,
        override val notes: List<NoteUml>,
        override val modifier: ModifierUml,
        override val accessControl: AccessControlUml?,
    ) : AttributeUml(name, notes, modifier, accessControl) {
        data class Argument(val name: String, val type: String)
    }
}

@UmlMarker
interface AnyBasicAttributeScopeDsl : ModifierDsl, AccessControlDsl {
    fun notes(init: NoteAttributeDsl.() -> Unit)
}

class AnyBasicAttributeScopeBuilder(
    val accessControlBuilder: AccessControlBuilder = AccessControlBuilder(),
    val modifierBuilder: ModifierBuilder = ModifierBuilder(),
    val notesBuilder: NotesBuilder = NotesBuilder()
) : AnyBasicAttributeScopeDsl,
    AccessControlDsl by accessControlBuilder,
    ModifierDsl by modifierBuilder {

    override fun notes(init: NoteAttributeDsl.() -> Unit) {
        notesBuilder.apply(init)
    }
}

@UmlMarker
interface AnyMethodAttributeScopeDsl : AnyBasicAttributeScopeDsl {
    fun argument(name: String, type: String)
}

class AnyMethodAttributeScopeBuilder(
    val anyAttributeScopeBuilder: AnyBasicAttributeScopeBuilder = AnyBasicAttributeScopeBuilder()
) : AnyMethodAttributeScopeDsl,
    AnyBasicAttributeScopeDsl by anyAttributeScopeBuilder {
    private val _arguments = mutableListOf<AttributeUml.Method.Argument>()
    val arguments: List<AttributeUml.Method.Argument> get() = _arguments.toList()

    override fun argument(name: String, type: String) {
        _arguments.add(AttributeUml.Method.Argument(name = name, type = type))
    }
}

@UmlMarker
interface AnyEnumAttributeScopeDsl : AnyBasicAttributeScopeDsl {
    fun argument(name: String, type: String)
}

class AnyEnumAttributeScopeBuilder(
    val anyAttributeScopeBuilder: AnyBasicAttributeScopeBuilder = AnyBasicAttributeScopeBuilder()
) : AnyEnumAttributeScopeDsl,
    AnyBasicAttributeScopeDsl by anyAttributeScopeBuilder {
    private val _arguments = mutableListOf<AttributeUml.EnumCase.Argument>()
    val arguments: List<AttributeUml.EnumCase.Argument> get() = _arguments.toList()

    override fun argument(name: String, type: String) {
        _arguments.add(AttributeUml.EnumCase.Argument(name = name, type = type))
    }
}

@UmlMarker
interface PropertyDsl {
    fun property(name: String, type: String, scope: AnyBasicAttributeScopeDsl.() -> Unit = {}): AttributeUml.Property
}

@UmlMarker
interface MethodDsl {
    fun method(
        name: String,
        returnType: String? = null,
        scope: AnyMethodAttributeScopeDsl.() -> Unit = {}
    ): AttributeUml.Method
}

@UmlMarker
interface EnumCaseDsl {
    fun case(name: String, scope: AnyEnumAttributeScopeDsl.() -> Unit = {}): AttributeUml.EnumCase
}

class AttributeBuilder : PropertyDsl, MethodDsl, EnumCaseDsl {
    private val _attributes = mutableListOf<AttributeUml>()
    val attributes: List<AttributeUml> get() = _attributes.toList()

    override fun property(name: String, type: String, scope: AnyBasicAttributeScopeDsl.() -> Unit): AttributeUml.Property {
        val scopeBuilder = AnyBasicAttributeScopeBuilder().apply(scope)
        return AttributeUml.Property(
            name = name,
            type = type,
            notes = scopeBuilder.notesBuilder.notes,
            modifier = scopeBuilder.modifierBuilder.build(),
            accessControl = scopeBuilder.accessControlBuilder.build()
        ).apply(_attributes::add)
    }

    override fun method(
        name: String,
        returnType: String?,
        scope: AnyMethodAttributeScopeDsl.() -> Unit
    ): AttributeUml.Method {
        val scopeBuilder = AnyMethodAttributeScopeBuilder().apply(scope)
        return AttributeUml.Method(
            name = name,
            returnType = returnType,
            arguments = scopeBuilder.arguments,
            notes = scopeBuilder.anyAttributeScopeBuilder.notesBuilder.notes,
            modifier = scopeBuilder.anyAttributeScopeBuilder.modifierBuilder.build(),
            accessControl = scopeBuilder.anyAttributeScopeBuilder.accessControlBuilder.build()
        ).apply(_attributes::add)
    }

    override fun case(name: String, scope: AnyEnumAttributeScopeDsl.() -> Unit): AttributeUml.EnumCase {
        val scopeBuilder = AnyEnumAttributeScopeBuilder().apply(scope)
        return AttributeUml.EnumCase(
            name = name,
            arguments = scopeBuilder.arguments,
            notes = scopeBuilder.anyAttributeScopeBuilder.notesBuilder.notes,
            modifier = scopeBuilder.anyAttributeScopeBuilder.modifierBuilder.build(),
            accessControl = scopeBuilder.anyAttributeScopeBuilder.accessControlBuilder.build()
        ).apply(_attributes::add)
    }
}
