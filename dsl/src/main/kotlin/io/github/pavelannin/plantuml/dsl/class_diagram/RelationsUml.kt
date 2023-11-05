package io.github.pavelannin.plantuml.dsl.class_diagram

data class RelationsUml(
    val from: DeclaringElementUml,
    val to: DeclaringElementUml,
    val type: Type,
) {

    enum class Type {
        Extension,
        Composition,
        Aggregation,
        SolidLine,
        DashedLine,
        DirectedSolidLine,
        DirectedDashedLine,
    }
}

@UmlMarker
interface RelationsDsl {
    infix fun DeclaringElementUml.extension(to: DeclaringElementUml)
    infix fun DeclaringElementUml.composition(to: DeclaringElementUml)
    infix fun DeclaringElementUml.aggregation(to: DeclaringElementUml)
    infix fun DeclaringElementUml.solidLine(to: DeclaringElementUml)
    infix fun DeclaringElementUml.dashedLine(to: DeclaringElementUml)
    infix fun DeclaringElementUml.directedSolidLine(to: DeclaringElementUml)
    infix fun DeclaringElementUml.directedDashedLine(to: DeclaringElementUml)

    infix fun DeclaringElementUml.extension(to: List<DeclaringElementUml>) {
        to.map { this extension it }
    }

    infix fun DeclaringElementUml.composition(to: List<DeclaringElementUml>) {
        to.map { this composition it }
    }

    infix fun DeclaringElementUml.aggregation(to: List<DeclaringElementUml>) {
        to.map { this aggregation it }
    }

    infix fun DeclaringElementUml.solidLine(to: List<DeclaringElementUml>) {
        to.map { this solidLine it }
    }

    infix fun DeclaringElementUml.dashedLine(to: List<DeclaringElementUml>) {
        to.map { this dashedLine it }
    }

    infix fun DeclaringElementUml.directedSolidLine(to: List<DeclaringElementUml>) {
        to.map { this directedSolidLine it }
    }

    infix fun DeclaringElementUml.directedDashedLine(to: List<DeclaringElementUml>) {
        to.map { this directedDashedLine it }
    }
}

class RelationsBuilder : RelationsDsl {
    private val _relations = mutableListOf<RelationsUml>()
    val relations: List<RelationsUml> get() = _relations.toList()

    override fun DeclaringElementUml.extension(to: DeclaringElementUml) {
        RelationsUml(from = this, to = to, type = RelationsUml.Type.Extension)
            .apply(_relations::add)
    }

    override fun DeclaringElementUml.composition(to: DeclaringElementUml) {
        RelationsUml(from = this, to = to, type = RelationsUml.Type.Composition)
            .apply(_relations::add)
    }

    override fun DeclaringElementUml.aggregation(to: DeclaringElementUml) {
        RelationsUml(from = this, to = to, type = RelationsUml.Type.Aggregation)
            .apply(_relations::add)
    }

    override fun DeclaringElementUml.solidLine(to: DeclaringElementUml) {
        RelationsUml(from = this, to = to, type = RelationsUml.Type.SolidLine)
            .apply(_relations::add)
    }

    override fun DeclaringElementUml.dashedLine(to: DeclaringElementUml) {
        RelationsUml(from = this, to = to, type = RelationsUml.Type.DashedLine)
            .apply(_relations::add)
    }

    override fun DeclaringElementUml.directedSolidLine(to: DeclaringElementUml) {
        RelationsUml(from = this, to = to, type = RelationsUml.Type.DirectedSolidLine)
            .apply(_relations::add)
    }

    override fun DeclaringElementUml.directedDashedLine(to: DeclaringElementUml) {
        RelationsUml(from = this, to = to, type = RelationsUml.Type.DirectedDashedLine)
            .apply(_relations::add)
    }
}
