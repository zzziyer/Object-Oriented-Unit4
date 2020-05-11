import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.common.AttributeQueryType;
import com.oocourse.uml2.interact.common.OperationQueryType;
import com.oocourse.uml2.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml2.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml2.interact.exceptions.user.UmlRule009Exception;
import com.oocourse.uml2.interact.format.UmlGeneralInteraction;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class MyUmlGeneralInteraction implements UmlGeneralInteraction {
    private Data data;
    private DataState dataState;
    private DataOrder dataOrder;

    public MyUmlGeneralInteraction(UmlElement... elements) {
        this.data = new Data(elements);
        this.dataState = new DataState(elements);
        this.dataOrder = new DataOrder(elements);
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
    public int getClassAttributeCount(String s,
                                      AttributeQueryType attributeQueryType)
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
    public List<AttributeClassInformation> getInformationNotHidden(
            String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        return data.unhidden(s);
    }

    @Override
    public int getParticipantCount(String s)
            throws InteractionNotFoundException,
            InteractionDuplicatedException {
        ArrayList<String> temp = new ArrayList<>();
        return dataOrder.getParNum(s, temp);
    }

    @Override
    public int getMessageCount(String s)
            throws InteractionNotFoundException,
            InteractionDuplicatedException {
        return dataOrder.getMessNum(s);
    }

    @Override
    public int getIncomingMessageCount(String s, String s1)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        return dataOrder.getIncomNum(s, s1);
    }

    @Override
    public void checkForUml002() throws UmlRule002Exception {
        data.rule2();
    }

    @Override
    public void checkForUml008() throws UmlRule008Exception {
        data.rule8();
    }

    @Override
    public void checkForUml009() throws UmlRule009Exception {
        data.rule9();
    }

    @Override
    public int getStateCount(String s)
            throws StateMachineNotFoundException,
            StateMachineDuplicatedException {
        HashSet<String> temp = new HashSet<>();
        return dataState.getStateNum(s, temp);
    }

    @Override
    public int getTransitionCount(String s)
            throws StateMachineNotFoundException,
            StateMachineDuplicatedException {
        HashMap<String, ArrayList<String>> temp = new HashMap<>();
        return dataState.getTrainsNum(s, temp);
    }

    @Override
    public int getSubsequentStateCount(String s, String s1)
            throws StateMachineNotFoundException,
            StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        return dataState.getSubNum(s, s1);
    }
}
