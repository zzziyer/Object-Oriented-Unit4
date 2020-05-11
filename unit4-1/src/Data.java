import com.oocourse.uml1.interact.common.AttributeClassInformation;
import com.oocourse.uml1.interact.common.AttributeQueryType;
import com.oocourse.uml1.interact.common.OperationQueryType;
import com.oocourse.uml1.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml1.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml1.models.common.Direction;
import com.oocourse.uml1.models.common.ElementType;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlAssociation;
import com.oocourse.uml1.models.elements.UmlAssociationEnd;
import com.oocourse.uml1.models.elements.UmlAttribute;
import com.oocourse.uml1.models.elements.UmlClass;
import com.oocourse.uml1.models.elements.UmlElement;
import com.oocourse.uml1.models.elements.UmlGeneralization;
import com.oocourse.uml1.models.elements.UmlInterface;
import com.oocourse.uml1.models.elements.UmlInterfaceRealization;
import com.oocourse.uml1.models.elements.UmlOperation;
import com.oocourse.uml1.models.elements.UmlParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Data {
    private HashMap<String, ArrayList<String>> interGene
            = new HashMap<>();
    private HashSet<String> dulClassName = new HashSet<>();
    private HashMap<String, MyClass> myClassHashMap
            = new HashMap<>();
    private HashMap<String, MyOperation> myOperationHashMap
            = new HashMap<>();
    private HashMap<String, UmlClass> umlClass
            = new HashMap<>();
    private HashMap<String, UmlAttribute> umlAttribute
            = new HashMap<>();
    private HashMap<String, UmlAssociation> umlAssociation
            = new HashMap<>();
    private HashMap<String, UmlAssociationEnd> umlAssociationEnd
            = new HashMap<>();
    private HashMap<String, UmlGeneralization> umlGeneralization
            = new HashMap<>();
    private HashMap<String, UmlInterface> umlInterface
            = new HashMap<>();
    private HashMap<String, UmlInterfaceRealization> umlInterfaceRealization
            = new HashMap<>();
    private HashMap<String, UmlOperation> umlOperation
            = new HashMap<>();
    private HashMap<String, UmlParameter> umlParameter
            = new HashMap<>();
    private HashMap<String, String> allClassName = new HashMap<>();

    public Data(UmlElement... elements) {
        int size = elements.length;
        for (int i = 0; i < size; i++) {
            UmlElement temp = elements[i];
            String s = temp.getId();
            if (temp.getElementType()
                    == ElementType.UML_CLASS) {
                umlClass.put(s, (UmlClass) temp);
            } else if (temp.getElementType()
                    == ElementType.UML_ATTRIBUTE) {
                umlAttribute.put(s, (UmlAttribute) temp);
            } else if (temp.getElementType()
                    == ElementType.UML_ASSOCIATION) {
                umlAssociation.put(s, (UmlAssociation) temp);
            } else if (temp.getElementType()
                    == ElementType.UML_ASSOCIATION_END) {
                umlAssociationEnd.put(s, (UmlAssociationEnd) temp);
            } else if (temp.getElementType()
                    == ElementType.UML_GENERALIZATION) {
                umlGeneralization.put(s, (UmlGeneralization) temp);
            } else if (temp.getElementType()
                    == ElementType.UML_INTERFACE) {
                umlInterface.put(s, (UmlInterface) temp);
            } else if (temp.getElementType()
                    == ElementType.UML_INTERFACE_REALIZATION) {
                umlInterfaceRealization.put(s, (UmlInterfaceRealization) temp);
            } else if (temp.getElementType()
                    == ElementType.UML_OPERATION) {
                umlOperation.put(s, (UmlOperation) temp);
            } else {
                umlParameter.put(s, (UmlParameter) temp);
            }
        }
        bulid();
    }

    public List<AttributeClassInformation> unhidden(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!allClassName.containsKey(s)) {
            throw new ClassNotFoundException(s);
        } else if (dulClassName.contains(s)) {
            throw new ClassDuplicatedException(s);
        } else {
            return myClassHashMap.get(allClassName.get(s)).
                    fiadUnhidden(myClassHashMap);
        }
    }

    public List<String> classImpInterList(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!allClassName.containsKey(s)) {
            throw new ClassNotFoundException(s);
        } else if (dulClassName.contains(s)) {
            throw new ClassDuplicatedException(s);
        } else {
            HashSet<String> inter = myClassHashMap.get(allClassName.get(s))
                    .getImpInterList(myClassHashMap);
            HashSet<String> temp = new HashSet<>();
            HashSet<String> temp2 = new HashSet<>();
            HashSet<String> temp3 = new HashSet<>();
            temp.addAll(inter);
            while (temp.size() != 0) {
                for (String k :
                        temp) {
                    if (interGene.containsKey(k)) {
                        temp3.addAll(interGene.get(k));
                    }
                }
                temp.addAll(temp3);
                temp3.clear();
                for (String k :
                        temp) {
                    if (!inter.contains(k)) {
                        inter.add(k);
                        temp2.add(k);
                    }
                }
                temp.clear();
                temp.addAll(temp2);
                temp2.clear();
            }
            ArrayList<String> re = new ArrayList<>();
            for (String k :
                    inter) {
                re.add(umlInterface.get(k).getName());
            }
            return re;
        }
    }

    public int classOpNum(String s, OperationQueryType type) {
        int re;
        if (!allClassName.containsKey(s)) {
            re = -1;
        } else if (dulClassName.contains(s)) {
            re = -2;
        } else {
            re = myClassHashMap.get(allClassName.get(s)).getOpNum(type);
        }
        return re;
    }

    public int classAttrNum(String s, AttributeQueryType type) {
        int re;
        if (!allClassName.containsKey(s)) {
            re = -1;
        } else if (dulClassName.contains(s)) {
            re = -2;
        } else {
            re = myClassHashMap.get(allClassName.get(s)).
                    getAttrNum(type, myClassHashMap);
        }
        return re;
    }

    public int classAssoNum(String s) {
        int re;
        if (!allClassName.containsKey(s)) {
            re = -1;
        } else if (dulClassName.contains(s)) {
            re = -2;
        } else {
            re = myClassHashMap.get(allClassName.get(s)).
                    getAssoNum(myClassHashMap);
        }
        return re;
    }

    public List<String> classAssoList(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        ArrayList<String> re = new ArrayList<>();
        ArrayList<String> temp;
        if (!allClassName.containsKey(s)) {
            throw new ClassNotFoundException(s);
        } else if (dulClassName.contains(s)) {
            throw new ClassDuplicatedException(s);
        } else {
            temp = myClassHashMap.get(allClassName.get(s)).
                    getAssoList(myClassHashMap);
        }
        //System.out.println("temp = " + temp);
        for (String k :
                temp) {
            re.add(umlClass.get(k).getName());
        }
        return re;
    }

    public Map<Visibility, Integer> classOpVis(String s, String s1)
            throws ClassNotFoundException, ClassDuplicatedException {
        HashMap<Visibility, Integer> re = new HashMap<>();
        if (!allClassName.containsKey(s)) {
            throw new ClassNotFoundException(s);
        } else if (dulClassName.contains(s)) {
            throw new ClassDuplicatedException(s);
        } else {
            re = myClassHashMap.get(allClassName.get(s)).getOpVis(s1);
        }
        return re;
    }

    public Visibility classAttrVis(String s, String s1)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        if (!allClassName.containsKey(s)) {
            throw new ClassNotFoundException(s);
        } else if (dulClassName.contains(s)) {
            throw new ClassDuplicatedException(s);
        } else {
            return myClassHashMap.get(allClassName.get(s)).
                    getAttrVis(s1, myClassHashMap);
        }
    }

    public String classTopParent(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!allClassName.containsKey(s)) {
            throw new ClassNotFoundException(s);
        } else if (dulClassName.contains(s)) {
            throw new ClassDuplicatedException(s);
        } else {
            return myClassHashMap.get(allClassName.get(s)).
                    getTopParent(myClassHashMap);
        }
    }

    public void bulid() {
        buildClass();
        buildOperation();
        buildParameter();
        buildAttribute();
        buildAssociation();
        buildGeneralization();
        buildInterRealize();
    }

    private void buildInterRealize() {
        for (UmlInterfaceRealization k :
                umlInterfaceRealization.values()) {
            myClassHashMap.get(k.getSource()).
                    addInter(umlInterface.get(k.getTarget()));
        }
    }

    private void buildGeneralization() {
        for (UmlGeneralization k :
                umlGeneralization.values()) {
            if (umlClass.containsKey(k.getSource())) {
                myClassHashMap.get(k.getSource()).addParent(k.getTarget());
            } else {
                if (interGene.containsKey(k.getSource())) {
                    ArrayList<String> temp = new ArrayList<>();
                    temp.addAll(interGene.get(k.getSource()));
                    temp.add(k.getTarget());
                    interGene.replace(k.getSource(), temp);
                } else {
                    ArrayList<String> temp = new ArrayList<>();
                    temp.add(k.getTarget());
                    interGene.put(k.getSource(), temp);
                }
            }
        }
    }

    private void buildAssociation() {
        for (UmlAssociation k :
                umlAssociation.values()) {
            String d1 = k.getEnd1();
            String d2 = k.getEnd2();
            String id1 = umlAssociationEnd.get(d1).getReference();
            String id2 = umlAssociationEnd.get(d2).getReference();
            if (umlClass.containsKey(id1) && umlClass.containsKey(id2)) {
                myClassHashMap.get(id1).putAssoClass(myClassHashMap.get(id2));
                myClassHashMap.get(id2).putAssoClass(myClassHashMap.get(id1));
                myClassHashMap.get(id1).addAssoNum();
                myClassHashMap.get(id2).addAssoNum();
            } else if (umlClass.containsKey(id1)) {
                myClassHashMap.get(id1).addAssoNum();
            } else if (umlClass.containsKey(id2)) {
                myClassHashMap.get(id2).addAssoNum();
            }
        }
    }

    private void buildParameter() {
        for (UmlParameter k :
                umlParameter.values()) {
            MyOperation temp = myOperationHashMap.get(k.getParentId());
            if (k.getDirection() == Direction.IN) {
                temp.setIn(temp.getIn() + 1);
            } else {
                temp.setOut(temp.getOut() + 1);
            }
            myOperationHashMap.replace(k.getParentId(), temp);
        }
        //put operations into class
        for (MyOperation k :
                myOperationHashMap.values()) {
            String pid = k.getPid();
            if (myClassHashMap.containsKey(pid)) {
                myClassHashMap.get(pid).putOperation(k);
            }
        }
    }

    private void buildAttribute() {
        for (UmlAttribute k :
                umlAttribute.values()) {
            if (myClassHashMap.containsKey(k.getParentId())) {
                myClassHashMap.get(k.getParentId()).putAttribute(k);
            }
        }
    }

    private void buildOperation() {
        for (UmlOperation k :
                umlOperation.values()) {
            int temp = 0;
            if (k.getVisibility() == Visibility.PUBLIC) {
                temp = 1;
            } else if (k.getVisibility() == Visibility.PROTECTED) {
                temp = 2;
            } else if (k.getVisibility() == Visibility.PRIVATE) {
                temp = 3;
            } else if (k.getVisibility() == Visibility.PACKAGE) {
                temp = 4;
            } else { //default
                temp = 5;
            }
            myOperationHashMap.put(k.getId(),
                    new MyOperation(k.getId(),
                            k.getParentId(), k.getName(), temp));
        }
    }

    private void buildClass() {
        for (UmlClass k :
                umlClass.values()) {
            String s = k.getName();
            if (allClassName.keySet().contains(s)) {
                myClassHashMap.put(k.getId(),
                        new MyClass(k.getId(), s, true));
                dulClassName.add(s);
            } else {
                allClassName.put(s, k.getId());
                myClassHashMap.put(k.getId(),
                        new MyClass(k.getId(), s, false));
            }
        }
        for (String k :
                allClassName.values()) {
            String n = myClassHashMap.get(k).getName();
            myClassHashMap.put(k,
                    new MyClass(k, n, true));
        }
    }

    public int getClassNum() {
        return umlClass.size();
    }
}
