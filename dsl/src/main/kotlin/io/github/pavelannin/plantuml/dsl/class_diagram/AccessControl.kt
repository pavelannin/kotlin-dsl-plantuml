package io.github.pavelannin.plantuml.dsl.class_diagram

data class AccessControlUml(val type: Type) {
    enum class Type { Public, Private, PrivatePackage, Protected }
}

@UmlMarker
interface AccessControlDsl {
    var accessControl: AccessControlUml.Type?
}

class AccessControlBuilder : AccessControlDsl {
    override var accessControl: AccessControlUml.Type? = null

    fun build(): AccessControlUml? = accessControl?.let(::AccessControlUml)
}
