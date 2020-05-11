import com.oocourse.uml1.interact.common.AttributeClassInformation;
import com.oocourse.uml1.interact.common.AttributeQueryType;
import com.oocourse.uml1.interact.common.OperationQueryType;
import com.oocourse.uml1.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml1.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml1.interact.format.UmlInteraction;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlElement;

import java.util.List;
import java.util.Map;

public class MyUmlInteraction implements UmlInteraction {
    private Data data;

    public MyUmlInteraction(UmlElement... elements) {
        this.data = new Data(elements);
    }

    @Override
    public int getClassCount() {
        return data.getClassNum();
    }

    @Override
    public int getClassOperationCount(String s,
                                      OperationQueryType operationQueryType)
            throws ClassNotFoundException, ClassDuplicatedException {
        int re = data.classOpNum(s, operationQueryType);
        if (re == -1) {
            throw new ClassNotFoundException(s);
        } else if (re == -2) {
            throw new ClassDuplicatedException(s);
        }
        return re;
    }

    @Override
    public int getClassAttributeCount(
            String s, AttributeQueryType attributeQueryType)
            throws ClassNotFoundException, ClassDuplicatedException {
        int re = data.classAttrNum(s, attributeQueryType);
        if (re == -1) {
            throw new ClassNotFoundException(s);
        } else if (re == -2) {
            throw new ClassDuplicatedException(s);
        }
        return re;
    }

    @Override
    public int getClassAssociationCount(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        int re = data.classAssoNum(s);
        if (re == -1) {
            throw new ClassNotFoundException(s);
        } else if (re == -2) {
            throw new ClassDuplicatedException(s);
        }
        return re;
    }

    @Override
    public List<String> getClassAssociatedClassList(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        return data.classAssoList(s);
    }

    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(
            String s, String s1)
            throws ClassNotFoundException, ClassDuplicatedException {
        return data.classOpVis(s, s1);
    }

    @Override
    public Visibility getClassAttributeVisibility(String s, String s1)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        return data.classAttrVis(s, s1);
    }

    @Override
    public String getTopParentClass(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        return data.classTopParent(s);
    }

    @Override
    public List<String> getImplementInterfaceList(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        return data.classImpInterList(s);
    }

    @Override
    public List<AttributeClassInformation> getInformationNotHidden(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        return data.unhidden(s);
    }
}
