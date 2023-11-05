package io.github.pavelannin.plantuml.dsl.class_diagram

data class ModifierUml(val isStatic: Boolean, val isAbstract: Boolean)

@UmlMarker
interface ModifierDsl {
    var isStatic: Boolean
    var isAbstract: Boolean
}

class ModifierBuilder : ModifierDsl {
    override var isStatic: Boolean = false
    override var isAbstract: Boolean = false

    fun build(): ModifierUml = ModifierUml(isStatic = isStatic, isAbstract = isAbstract)
}
