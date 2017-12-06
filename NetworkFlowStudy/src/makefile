JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	graphCode/Edge.java \
	graphCode/GraphInput.java \
	graphCode/InputLib.java \
	graphCode/KeyboardReader.java \
	graphCode/SimpleGraph.java \
	graphCode/Vertex.java \
	graphGenerationCode/Bipartite/BipartiteGraph.java \
	graphGenerationCode/FixedDegree/RandomGraph.java \
	graphGenerationCode/Mesh/MeshGenerator.java \
	graphGenerationCode/Random/BuildGraph.java \
	networkflowstudy/logging.java \
	networkflowstudy/utils.java \
	networkflowstudy/SaveOutput.java \
	networkflowstudy/MaxFlow.java \
	networkflowstudy/PreflowPush.java \
	networkflowstudy/ScalingMaxFlow.java \
	tcss543.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	rm graphCode/*.class \
	rm graphGenerationCode/Bipartite/*.class \
	rm graphGenerationCode/FixedDegree/*.class \
	rm graphGenerationCode/Mesh/*.class \
	rm graphGenerationCode/Random/*.class \
	rm networkflowstudy/*.class \
	$(RM) *.class
