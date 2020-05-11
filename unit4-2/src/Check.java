import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml2.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml2.interact.exceptions.user.UmlRule009Exception;
import com.oocourse.uml2.models.elements.UmlClass;
import com.oocourse.uml2.models.elements.UmlClassOrInterface;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlInterface;
import com.oocourse.uml2.models.elements.UmlInterfaceRealization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Check {
    private Boolean ori = true;
    private HashMap<String, ArrayList<String>> edge = new HashMap<>();
    private HashMap<String, Integer> classAndInter = new HashMap<>();
    private HashMap<String, UmlClass> allclass = new HashMap<>();
    private HashMap<String, UmlInterface> allinter = new HashMap<>();

    public Check() {
    }

    public void build(
            HashSet<UmlGeneralization> gene,
            HashSet<UmlInterfaceRealization> real,
            HashMap<String, UmlClass> classes,
            HashMap<String, UmlInterface> inters) {
        allclass.putAll(classes);
        allinter.putAll(inters);
        for (String k :
                classes.keySet()) {
            classAndInter.put(k, 0);
        }
        for (String k :
                inters.keySet()) {
            classAndInter.put(k, 0);
        }
        for (UmlGeneralization k :
                gene) {
            String source = k.getSource();
            String target = k.getTarget();
            ArrayList<String> temp = new ArrayList<>();
            if (edge.containsKey(source)) {
                temp.addAll(edge.get(source));
                temp.add(target);
                edge.replace(source, temp);
            } else {
                temp.add(target);
                edge.put(source, temp);
            }
        }
        for (UmlInterfaceRealization k :
                real) {
            String source = k.getSource();
            String target = k.getTarget();
            ArrayList<String> temp = new ArrayList<>();
            if (edge.containsKey(source)) {
                temp.addAll(edge.get(source));
                temp.add(target);
                edge.replace(source, temp);
            } else {
                temp.add(target);
                edge.put(source, temp);
            }
        }
    }

    public void rule002(HashSet<MyClass> classes)
            throws UmlRule002Exception {
        HashSet<AttributeClassInformation> re = new HashSet<>();
        for (MyClass k :
                classes) {
            ArrayList<String> temp1 = k.getEndName();
            ArrayList<String> temp2 = k.getAllAttr();
            HashSet<String> temp = new HashSet<>();
            for (String s :
                    temp1) {
                if (s == null) {
                    continue;
                }
                if (temp.contains(s)) {
                    re.add(new AttributeClassInformation(s, k.getName()));
                } else {
                    temp.add(s);
                }
            }
            for (String s :
                    temp2) {
                if (s == null) {
                    continue;
                }
                if (temp.contains(s)) {
                    re.add(new AttributeClassInformation(s, k.getName()));
                } else {
                    temp.add(s);
                }
            }
        }
        if (re.size() != 0) {
            throw new UmlRule002Exception(re);
        }
    }

    public void rule008(
            HashSet<UmlGeneralization> gene,
            HashSet<UmlInterfaceRealization> real,
            HashMap<String, UmlClass> classes,
            HashMap<String, UmlInterface> inters
    ) throws UmlRule008Exception {
        HashSet<UmlClassOrInterface> re = new HashSet<>();
        if (ori) {
            build(gene, real, classes, inters);
            ori = false;
        }
        for (String s :
                classAndInter.keySet()) {
            int k = dfs1(s, s, 0, 0);
            if (k != 0) {
                if (allclass.containsKey(s)) {
                    re.add(allclass.get(s));
                } else {
                    re.add(allinter.get(s));
                }
            }
            for (String temp :
                    classAndInter.keySet()) {
                classAndInter.replace(temp, 0);
            }
        }
        if (re.size() != 0) {
            throw new UmlRule008Exception(re);
        }
    }

    public int dfs1(String s, String init, int cnt, int tag) {
        classAndInter.replace(s, classAndInter.get(s) + 1);
        int x = tag;
        if (cnt != 0 && s.equals(init)) {
            x = 1;
        }
        if (x == 1) {
            return x;
        }
        if (classAndInter.get(s) > 1) {
            return 0;
        }
        if (edge.containsKey(s)) {
            for (String k :
                    edge.get(s)) {
                int temp = dfs1(k, init, cnt + 1, x);
                if (temp == 1) {
                    return temp;
                }
            }
        }
        return x;
    }

    public void rule009(
            HashSet<UmlGeneralization> gene,
            HashSet<UmlInterfaceRealization> real,
            HashMap<String, UmlClass> classes,
            HashMap<String, UmlInterface> inters
    ) throws UmlRule009Exception {
        HashSet<UmlClassOrInterface> re = new HashSet<>();
        if (ori) {
            build(gene, real, classes, inters);
            ori = false;
        }
        for (String s :
                classAndInter.keySet()) {
            int k = dfs2(s, 0);
            if (k != 0) {
                if (allclass.containsKey(s)) {
                    re.add(allclass.get(s));
                } else {
                    re.add(allinter.get(s));
                }
            }
            for (String temp :
                    classAndInter.keySet()) {
                classAndInter.replace(temp, 0);
            }
        }
        if (re.size() != 0) {
            throw new UmlRule009Exception(re);
        }
    }

    public int dfs2(String s, int tag) {
        int x = tag;
        if (classAndInter.get(s) != 0) {
            x = 1;
        }
        if (x == 1) {
            return x;
        }
        classAndInter.replace(s, classAndInter.get(s) + 1);
        if (edge.containsKey(s)) {
            for (String k :
                    edge.get(s)) {
                int temp = dfs2(k, x);
                if (temp == 1) {
                    return temp;
                }
            }
        }
        return x;
    }
}
