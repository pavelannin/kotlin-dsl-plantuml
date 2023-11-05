package io.github.pavelannin.plantuml.dsl.class_diagram

@DslMarker
annotation class UmlMarker

@UmlMarker
fun classDiagram(init: ClassDiagramDsl.() -> Unit): ClassDiagramUml = ClassDiagramBuilder().apply(init).build()

data class ClassDiagramUml(
    val elements: List<DeclaringElementUml>,
    val relations: List<RelationsUml>,
)

@UmlMarker
interface ClassDiagramDsl : DeclaringElementDsl, RelationsDsl

class ClassDiagramBuilder(
    val declaringElementBuilder: DeclaringElementBuilder = DeclaringElementBuilder(),
    val relationsBuilder: RelationsBuilder = RelationsBuilder(),
) : ClassDiagramDsl,
    DeclaringElementDsl by declaringElementBuilder,
    RelationsDsl by relationsBuilder {

    fun build(): ClassDiagramUml = ClassDiagramUml(
        elements = declaringElementBuilder.elements,
        relations = relationsBuilder.relations,
    )
}
