import com.oocourse.uml1.interact.common.AttributeClassInformation;
import com.oocourse.uml1.interact.common.AttributeQueryType;
import com.oocourse.uml1.interact.common.OperationQueryType;
import com.oocourse.uml1.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlAttribute;
import com.oocourse.uml1.models.elements.UmlInterface;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MyClass {
    private String name;
    private HashSet<String> dulAttName = new HashSet<>();
    private String id;
    private HashMap<String, UmlAttribute> attribute
            = new HashMap<>();  //name->att
    private String parentId = null;
    private ArrayList<String> allParents = new ArrayList<>();
    private boolean isDuplicated;
    private HashMap<String, UmlInterface> interfacee
            = new HashMap<>();  //id->inter
    private HashMap<String, MyOperation> operation
            = new HashMap<>();  //id->op
    private int assoNum;
    private HashMap<String, MyClass> assoClass = new HashMap<>();
    private boolean opClassify = true;
    private int[] opType = new int[4];
    //0->non_return  1->return  2->non_param  3->param
    private boolean parOri = true;
    private int parAssoNum = 0;
    private int attrNum = 0;
    private ArrayList<UmlAttribute> allAttr = new ArrayList<>();
    private int parentAttrNum = 0;
    private HashSet<String> parDulAtt = new HashSet<>();
    private HashMap<Pair<Pair<String, String>,
            Visibility>, UmlAttribute> parAttribute
            = new HashMap<>();  //name->att

    public MyClass(String id, String name, boolean isDuplicated) {
        this.id = id;
        this.name = name;
        this.isDuplicated = isDuplicated;
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return id;
    }

    public void putAttribute(UmlAttribute u) {
        attrNum++;
        allAttr.add(u);
        if (attribute.keySet().contains(u.getName())) {
            dulAttName.add(u.getName());
        } else {
            attribute.put(u.getName(), u);
        }
    }

    public void putOperation(MyOperation op) {
        operation.put(op.getId(), op);
    }

    public void putAssoClass(MyClass umlClass) {
        assoClass.put(umlClass.getId(), umlClass);
    }

    public void addAssoNum() {
        this.assoNum += 1;
    }

    public void addParent(String pid) {
        this.parentId = pid;
    }

    public void addInter(UmlInterface umlInterface) {
        interfacee.put(umlInterface.getId(), umlInterface);
    }

    public int getOpNum(OperationQueryType type) {
        int re;
        if (type == OperationQueryType.ALL) {
            re = operation.size();
        } else {
            if (opClassify) {
                opTrave();
                opClassify = false;
            }
            if (type == OperationQueryType.NON_RETURN) {
                re = opType[0];
            } else if (type == OperationQueryType.RETURN) {
                re = opType[1];
            } else if (type == OperationQueryType.NON_PARAM) {
                re = opType[2];
            } else {
                re = opType[3];
            }
        }
        return re;
    }

    public HashSet<String> getImpInterList(HashMap<String,
            MyClass> myclassmap) {
        if (parOri) {
            recursion(myclassmap);
            parOri = false;
        }
        Set<String> temp = interfacee.keySet();
        HashSet<String> re = new HashSet<>();
        re.addAll(temp);
        return re;
    }

    public ArrayList<AttributeClassInformation> fiadUnhidden(
            HashMap<String, MyClass> myclassmap) {
        if (parOri) {
            recursion(myclassmap);
            parOri = false;
        }
        ArrayList<AttributeClassInformation> re = new ArrayList<>();
        for (UmlAttribute k :
                attribute.values()) {
            if (k.getVisibility() != Visibility.PRIVATE) {
                re.add(new AttributeClassInformation(k.getName(),
                        myclassmap.get(k.getParentId()).getName()));
            }
        }
        for (Pair<Pair<String, String>, Visibility> k :
                parAttribute.keySet()) {
            if (k.getValue() != Visibility.PRIVATE) {
                UmlAttribute temp = parAttribute.get(k);
                re.add(new AttributeClassInformation(temp.getName(),
                        myclassmap.get(temp.getParentId()).getName()));
            }
        }
        return re;
    }

    public Visibility getAttrVis(String s1, HashMap<String, MyClass> myclassmap)
            throws AttributeNotFoundException, AttributeDuplicatedException {
        if (dulAttName.contains(s1)) {
            throw new AttributeDuplicatedException(name, s1);
        } else {
            if (parOri) {
                recursion(myclassmap);
                parOri = false;
            }
            if (parDulAtt.contains(s1)) {
                throw new AttributeDuplicatedException(name, s1);
            } else {
                int cnt = 0;
                ArrayList<UmlAttribute> temp = new ArrayList<>();
                for (Pair<Pair<String, String>, Visibility> k :
                        parAttribute.keySet()) {
                    if (k.getKey().getKey().equals(s1)) {
                        cnt++;
                        temp.add(parAttribute.get(k));
                    }
                }
                if ((attribute.containsKey(s1) && cnt > 0) ||
                        (!attribute.containsKey(s1) && cnt > 1)) {
                    throw new AttributeDuplicatedException(name, s1);
                } else if (attribute.containsKey(s1)) {
                    return attribute.get(s1).getVisibility();
                } else if (cnt == 1) {
                    return temp.get(0).getVisibility();
                } else {
                    throw new AttributeNotFoundException(name, s1);
                }
            }
        }
    }

    public HashMap<Visibility, Integer> getOpVis(String s) {
        HashMap<Visibility, Integer> vis = new HashMap<>();
        vis.put(Visibility.PUBLIC, 0);
        vis.put(Visibility.PROTECTED, 0);
        vis.put(Visibility.PRIVATE, 0);
        vis.put(Visibility.PACKAGE, 0);
        for (MyOperation k :
                operation.values()) {
            if (k.getName().equals(s)) {
                int temp = k.getVisiable();
                if (temp == 1 || temp == 5) {
                    vis.replace(Visibility.PUBLIC,
                            vis.get(Visibility.PUBLIC) + 1);
                } else if (temp == 2) {
                    vis.replace(Visibility.PROTECTED,
                            vis.get(Visibility.PROTECTED) + 1);
                } else if (temp == 3) {
                    vis.replace(Visibility.PRIVATE,
                            vis.get(Visibility.PRIVATE) + 1);
                } else {
                    vis.replace(Visibility.PACKAGE,
                            vis.get(Visibility.PACKAGE) + 1);
                }
            }
        }
        return vis;
    }

    public void opTrave() {
        for (MyOperation k :
                operation.values()) {
            if (k.getIn() > 0) {
                opType[3]++;
            } else {
                opType[2]++;
            }
            if (k.getOut() > 0) {
                opType[1]++;
            } else {
                opType[0]++;
            }
        }
    }

    public int getAssoNum(HashMap<String, MyClass> myclassmap) {
        if (parOri) {
            recursion(myclassmap);
            parOri = false;
        }
        return assoNum + parAssoNum;
    }

    public ArrayList<String> getAssoList(HashMap<String, MyClass> myclassmap) {
        if (parOri) {
            recursion(myclassmap);
            parOri = false;
        }
        ArrayList<String> re = new ArrayList<>();
        re.addAll(assoClass.keySet());
        return re;
    }

    public String getTopParent(HashMap<String, MyClass> myclassmap) {
        if (parOri) {
            recursion(myclassmap);
            parOri = false;
        }
        int size = allParents.size();
        if (size == 0) {
            return this.name;
        } else {
            return myclassmap.get(allParents.get(size - 1)).getName();
        }
    }

    public int getAttrNum(AttributeQueryType type,
                          HashMap<String, MyClass> myclassmap) {
        if (type == AttributeQueryType.SELF_ONLY) {
            return attrNum;
        } else {
            if (parOri) {
                recursion(myclassmap);
                parOri = false;
            }
            return parentAttrNum + attrNum;
        }
    }

    public void recursion(HashMap<String, MyClass> myclassmap) {
        MyClass p = this;
        while (p.parentId != null) {
            p = myclassmap.get(p.parentId);
            allParents.add(p.id);
            this.parentAttrNum += p.attrNum;
            for (UmlAttribute k :
                    p.attribute.values()) {
                this.parAttribute.put(new Pair<>(new Pair<>(
                        k.getName(), k.getParentId()), k.getVisibility()), k);
            }
            this.parAssoNum += p.assoNum;
            this.parDulAtt.addAll(p.dulAttName);
            this.interfacee.putAll(p.interfacee);
            this.assoClass.putAll(p.assoClass);
        }
    }

}
