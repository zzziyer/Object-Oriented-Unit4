import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlEvent;
import com.oocourse.uml2.models.elements.UmlFinalState;
import com.oocourse.uml2.models.elements.UmlOpaqueBehavior;
import com.oocourse.uml2.models.elements.UmlPseudostate;
import com.oocourse.uml2.models.elements.UmlRegion;
import com.oocourse.uml2.models.elements.UmlState;
import com.oocourse.uml2.models.elements.UmlStateMachine;
import com.oocourse.uml2.models.elements.UmlTransition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class DataState {
    private HashMap<String, UmlRegion> umlRegion
            = new HashMap<>();
    private HashMap<String, UmlStateMachine> umlStateMachine
            = new HashMap<>();
    private HashMap<String, UmlPseudostate> umlPseudostate
            = new HashMap<>();
    private HashMap<String, UmlState> umlState
            = new HashMap<>();
    private HashMap<String, UmlFinalState> umlFinalState
            = new HashMap<>();
    private HashMap<String, UmlEvent> umlEvent
            = new HashMap<>();
    private HashMap<String, UmlOpaqueBehavior> umlOpaqueBehavior
            = new HashMap<>();
    private HashMap<String, UmlTransition> umlTransition
            = new HashMap<>();
    private HashMap<String, String> allMachineName
            = new HashMap<>();
    private HashSet<String> dulMachineName
            = new HashSet<>();

    public DataState(UmlElement... elements) {
        int size = elements.length;
        for (int i = 0; i < size; i++) {
            UmlElement temp = elements[i];
            String s = temp.getId();
            if (temp.getElementType()
                    == ElementType.UML_REGION) {
                umlRegion.put(s, (UmlRegion) temp);
            } else if (temp.getElementType()
                    == ElementType.UML_STATE_MACHINE) {
                String tname = temp.getName();
                if (allMachineName.keySet().contains(tname)) {
                    dulMachineName.add(tname);
                } else {
                    allMachineName.put(tname, s);
                }
                umlStateMachine.put(s, (UmlStateMachine) temp);
            } else if (temp.getElementType()
                    == ElementType.UML_PSEUDOSTATE) {
                umlPseudostate.put(s, (UmlPseudostate) temp);
            } else if (temp.getElementType()
                    == ElementType.UML_STATE) {
                umlState.put(s, (UmlState) temp);
            } else if (temp.getElementType()
                    == ElementType.UML_FINAL_STATE) {
                umlFinalState.put(s, (UmlFinalState) temp);
            } else if (temp.getElementType()
                    == ElementType.UML_EVENT) {
                umlEvent.put(s, (UmlEvent) temp);
            } else if (temp.getElementType()
                    == ElementType.UML_OPAQUE_BEHAVIOR) {
                umlOpaqueBehavior.put(s, (UmlOpaqueBehavior) temp);
            } else if (temp.getElementType()
                    == ElementType.UML_TRANSITION) {
                umlTransition.put(s, (UmlTransition) temp);
            } else {
                //do nothing
            }
        }
    }

    public String findRegion(String machineId) {
        String re = "";
        for (UmlRegion k :
                umlRegion.values()) {
            if (k.getParentId().equals(machineId)) {
                re = k.getId();
            }
        }
        return re;
    }

    public int getStateNum(String s, HashSet<String> states)
            throws StateMachineNotFoundException,
            StateMachineDuplicatedException {
        if (!allMachineName.containsKey(s)) {
            throw new StateMachineNotFoundException(s);
        } else if (dulMachineName.contains(s)) {
            throw new StateMachineDuplicatedException(s);
        } else {
            int cnt = 0;
            String mid = allMachineName.get(s);
            String rid = findRegion(mid);
            for (UmlState k :
                    umlState.values()) {
                if (k.getParentId().equals(rid)) {
                    states.add(k.getId());
                    cnt++;
                }
            }
            for (UmlPseudostate k :
                    umlPseudostate.values()) {
                if (k.getParentId().equals(rid)) {
                    cnt++;
                }
            }
            for (UmlFinalState k :
                    umlFinalState.values()) {
                if (k.getParentId().equals(rid)) {
                    cnt++;
                }
            }
            return cnt;
        }
    }

    public int getTrainsNum(String s,
                            HashMap<String, ArrayList<String>> tr)
            throws StateMachineNotFoundException,
            StateMachineDuplicatedException {
        if (!allMachineName.containsKey(s)) {
            throw new StateMachineNotFoundException(s);
        } else if (dulMachineName.contains(s)) {
            throw new StateMachineDuplicatedException(s);
        } else {
            int cnt = 0;
            String mid = allMachineName.get(s);
            String rid = findRegion(mid);
            for (UmlTransition k :
                    umlTransition.values()) {
                if (k.getParentId().equals(rid)) {
                    String source = k.getSource();
                    String target = k.getTarget();
                    if (tr.containsKey(source)) {
                        ArrayList<String> temp = new ArrayList<>();
                        temp.addAll(tr.get(source));
                        temp.add(target);
                        tr.replace(source, temp);
                    } else {
                        ArrayList<String> temp = new ArrayList<>();
                        temp.add(target);
                        tr.put(source, temp);
                    }
                    cnt++;
                }
            }
            return cnt;
        }
    }

    public int getSubNum(String mname, String sname)
            throws StateMachineNotFoundException,
            StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        if (!allMachineName.containsKey(mname)) {
            throw new StateMachineNotFoundException(mname);
        } else if (dulMachineName.contains(mname)) {
            throw new StateMachineDuplicatedException(mname);
        } else {
            //String mid = allMachineName.get(mname);
            //String rid = findRegion(mid);
            ArrayList<String> name2Id = new ArrayList<>();
            HashSet<String> states = new HashSet<>();//id
            HashMap<String, ArrayList<String>> trans = new HashMap<>();
            //source-->umltrains.id
            getStateNum(mname, states);
            getTrainsNum(mname, trans);
            for (String k :
                    states) {
                UmlState temp = umlState.get(k);
                if (temp.getName().equals(sname)) {
                    name2Id.add(k);
                }
            }
            if (name2Id.size() == 0) {
                throw new StateNotFoundException(mname, sname);
            } else if (name2Id.size() > 1) {
                throw new StateDuplicatedException(mname, sname);
            } else {
                int re = findSub(name2Id.get(0), trans);
                return re;
            }
        }
    }

    public int findSub(String s, HashMap<String, ArrayList<String>> trans) {
        HashSet<String> ori = new HashSet<>();
        ori.add(s);
        int tag = 0;
        HashSet<String> temp = new HashSet<>();
        HashSet<String> temp2 = new HashSet<>();
        HashSet<String> temp3 = new HashSet<>();
        temp.addAll(ori);
        while (temp.size() != 0) {
            for (String k :
                    temp) {
                if (trans.containsKey(k)) {
                    temp3.addAll(trans.get(k));
                    if (temp3.contains(s)) {
                        tag = 1;
                    }
                }
            }
            temp.addAll(temp3);
            temp3.clear();
            for (String k :
                    temp) {
                if (!ori.contains(k)) {
                    ori.add(k);
                    temp2.add(k);
                }
            }
            temp.clear();
            temp.addAll(temp2);
            temp2.clear();
        }
        //System.out.println("tag = " + tag);
        if (tag == 1) {
            //System.out.println("!!!!!!!!!!!!!!");
            return ori.size();
        } else {
            return ori.size() - 1;
        }
    }
}
