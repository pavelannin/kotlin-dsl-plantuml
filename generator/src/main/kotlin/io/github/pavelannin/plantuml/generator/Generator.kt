package io.github.pavelannin.plantuml.generator

import io.github.pavelannin.plantuml.dsl.class_diagram.ClassDiagramUml

fun createPlantUml(uml: ClassDiagramUml): String = convertClassDiagramToPlantuml(uml)

fun ClassDiagramUml.plantUml(): String = createPlantUml(uml = this)
