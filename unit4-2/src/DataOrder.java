import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlEndpoint;
import com.oocourse.uml2.models.elements.UmlInteraction;
import com.oocourse.uml2.models.elements.UmlLifeline;
import com.oocourse.uml2.models.elements.UmlMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class DataOrder {
    private HashMap<String, UmlInteraction> umlInteraction
            = new HashMap<>();
    private HashMap<String, UmlLifeline> umlLifeline
            = new HashMap<>();
    private HashMap<String, UmlMessage> umlMessage
            = new HashMap<>();
    private HashMap<String, UmlEndpoint> umlEndpoint
            = new HashMap<>();

    private HashMap<String, String> allInterName
            = new HashMap<>();
    private HashSet<String> dulInterName
            = new HashSet<>();

    public DataOrder(UmlElement... elements) {
        int size = elements.length;
        for (int i = 0; i < size; i++) {
            UmlElement temp = elements[i];
            String s = temp.getId();
            if (temp.getElementType()
                    == ElementType.UML_INTERACTION) {
                String iname = temp.getName();
                if (allInterName.keySet().contains(iname)) {
                    dulInterName.add(iname);
                } else {
                    allInterName.put(iname, s);
                }
                umlInteraction.put(s, (UmlInteraction) temp);
            } else if (temp.getElementType()
                    == ElementType.UML_LIFELINE) {
                umlLifeline.put(s, (UmlLifeline) temp);
            } else if (temp.getElementType()
                    == ElementType.UML_MESSAGE) {
                umlMessage.put(s, (UmlMessage) temp);
            } else if (temp.getElementType()
                    == ElementType.UML_ENDPOINT) {
                umlEndpoint.put(s, (UmlEndpoint) temp);
            } else {
                //do nothing
            }
        }
    }

    public int getParNum(String s, ArrayList<String> lines)
            throws InteractionNotFoundException,
            InteractionDuplicatedException {
        if (!allInterName.containsKey(s)) {
            throw new InteractionNotFoundException(s);
        } else if (dulInterName.contains(s)) {
            throw new InteractionDuplicatedException(s);
        } else {
            int cnt = 0;
            String iid = allInterName.get(s);
            for (UmlLifeline k :
                    umlLifeline.values()) {
                if (k.getParentId().equals(iid)) {
                    lines.add(k.getId());
                    cnt++;
                }
            }
            return cnt;
        }
    }

    public int getMessNum(String s)
            throws InteractionNotFoundException,
            InteractionDuplicatedException {
        if (!allInterName.containsKey(s)) {
            throw new InteractionNotFoundException(s);
        } else if (dulInterName.contains(s)) {
            throw new InteractionDuplicatedException(s);
        } else {
            int cnt = 0;
            String iid = allInterName.get(s);
            for (UmlMessage k :
                    umlMessage.values()) {
                if (k.getParentId().equals(iid)) {
                    cnt++;
                }
            }
            return cnt;
        }
    }

    public int getIncomNum(String iname, String lname)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        if (!allInterName.containsKey(iname)) {
            throw new InteractionNotFoundException(iname);
        } else if (dulInterName.contains(iname)) {
            throw new InteractionDuplicatedException(iname);
        } else {
            ArrayList<String> lines = new ArrayList<>();
            ArrayList<String> name2Id = new ArrayList<>();
            getParNum(iname, lines);
            for (String k :
                    lines) {
                UmlLifeline temp = umlLifeline.get(k);
                if (temp.getName().equals(lname)) {
                    name2Id.add(k);
                }
            }
            if (name2Id.size() == 0) {
                throw new LifelineNotFoundException(iname, lname);
            } else if (name2Id.size() > 1) {
                throw new LifelineDuplicatedException(iname, lname);
            } else {
                String lid = name2Id.get(0);
                int cnt = 0;
                for (UmlMessage k :
                        umlMessage.values()) {
                    if (k.getTarget().equals(lid)) {
                        cnt++;
                    }
                }
                return cnt;
            }
        }
    }
}
