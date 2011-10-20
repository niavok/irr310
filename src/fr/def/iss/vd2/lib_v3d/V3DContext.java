// Copyright 2010 DEF
//
// This file is part of V3dScene.
//
// V3dScene is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// V3dScene is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with V3dScene.  If not, see <http://www.gnu.org/licenses/>.
package fr.def.iss.vd2.lib_v3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JPopupMenu;

import org.fenggui.FengGUI;
import org.fenggui.util.Alphabet;

import fr.def.iss.vd2.lib_v3d.camera.V3DCameraBinding;
import fr.def.iss.vd2.lib_v3d.element.TextureManager;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;
import fr.def.iss.vd2.lib_v3d.element.V3DIdAllocator;

/**
 * Un V3dContext relie tous les objets servant au rendu d'image 3d dans un composant
 * canvas d'awt.
 *<pre>
 * La plupart des classes sont associé à un context qui rassemble des méthodes 
 * globales à l'ensemble du rendu.
 *
 * Voici le fonctionnement général de la bibliothèque V3dScene.
 *
 * Tous le dessin est fait dans un composant lourd awt dédié au dessin, un canvas.
 * La classe V3DCanvas contient un canvas awt qui doit être ajouté dans l'arbre
 * de composants de l'interface graphique parent awt ou swing, comme n'importe
 * quel composant.
 *
 * Ensuite, il faut découper la surface du canvas en différentes zones qui pourront
 * afficher des choses indépendantes à l'aide de V3DCameraBinding.
 *
 * Un V3DCameraBinding représente une zone du canvas dans laquelle un dessin est
 * effectué.
 * L'utilisation la plus fréquente est de placer un seul V3DCameraBinding qui
 * occupe l'ensemble de la surface du canvas. Cependant, il est possible par
 * exemple de séparer la surface du canvas avec 4 bindings qui afficheront des
 * scènes différentes, ou différents points de vue de la même scene.
 * Il est possible de superposer les bindings, par exemple en créant un binding
 * occupant tout le fond et 2 autres, plus petits, en surimpression.
 *
 * Voici les 3 cas présentés si dessus:
 *
 *  * Cas 1 : 1 binding en fond :
 *   +-----------------------------------+
 *   |                                   |
 *   |                                   |
 *   |                                   |
 *   |                                   |
 *   |             (1)                   |
 *   |                                   |
 *   |                                   |
 *   |                                   |
 *   |                                   |
 *   +-----------------------------------+
 * 
 *  * Cas 3 : 4 bindings en fond:
 *   +-----------------+-----------------+
 *   |                 |                 |
 *   |       (1)       |       (2)       |
 *   |                 |                 |
 *   |                 |                 |
 *   +-----------------+-----------------+
 *   |                 |                 |
 *   |                 |                 |
 *   |       (3)       |       (4)       |
 *   |                 |                 |
 *   +-----------------------------------+
 * 
 *
 * Cas 3 : 1 binding en fond et 2 bindings superposés:
 *   +-----------------------------------+
 *   |                                   |
 *   |   +-----+                         |
 *   |   | (2) |                         |
 *   |   +-----+         +-----------+   |
 *   |             (1)   |           |   |
 *   |                   |    (3)    |   |
 *   |                   |           |   |
 *   |                   +-----------+   |
 *   |                                   |
 *   +-----------------------------------+
 *
 * Dans la zone délimitée par chacun des bindings, il est possible de dessiner une
 * scène par l'intermédiaire d'une camera (V3dCamera).
 *
 * Une scene (V3DScene) représente un ensemble d'éléments organisés sous
 * forme d'arbre.
 * Pour dessiner un vélo simple sur une route, on peut par exemple créer une
 * scène et y ajouter un élément "plan" pour la route et un élément "vélo",
 * lui meme décomposé en elements "roue" et "cadre".
 *
 * Un exemple d'arbre de scène:
 *
 *                       * racine de la scène
 *                       |
 *         +-------------+-------------+
 *         |                           |
 *         * plan (route)              * groupe (vélo)
 *                                     |
 *                                     |
 *    +-------------------------+------+--------------------+
 *    |                         |                           |
 *    * cylindre (roue avant)   * cylindre (roue arrière)   * boite (cadre)
 *
 *
 * Chaque élément dispose d'une position dans l'espace, d'une rotation et d'un
 * facteur d'echelle. Ces 3 paramètres sont relatifs à l'élément parent. Les
 * coordonées des roues seront données par rapport au groupe (vélo) et si l'on
 * veut déplacer le vélo, il suffit de modifier la position du groupe vélo à
 * intervalle régulier sans toucher à la position des roues et du cadre.
 *
 * Pour dessiner une scene dans un binding, il faut utiliser une 
 * camera (V3DCamera) afin de définir le point de vue de l'observateur par 
 * rapport à la scene. 
 * Chaque binding est associé à une caméra qui affiche une scène. Cependant, il
 * est possible d'avoir à peu près toutes les configurations imaginables :
 * - plusieurs caméra affichant la même scène selon plusieurs points de vue
 * - plusieurs bindings de tailles différentes affichant la même caméra
 * - plusieurs scènes contenant le même objet dans l'arbre de scene
 * - le même objet à plusieurs endroits dans le même arbre de scène.
 * - ...
 *
 * En plus du dessin des scènes par le point de vue des cameras, il est possible
 * de placer des éléments d'interface graphique comme du texte, des textfields,
 * ou autre au premier plan. Cela se fait via un objet GUI que chaque binding
 * possède.
 *
 * Le dessin global est rafraichi plusieurs fois par seconde et prend toujours
 * en compte dynamiquement n'importe quelle modification effectuée sur la
 * camera, les gui, les éléments des arbres de scène ou autre.
 *
 * La bibliothèque V3DScene fournit aussi des outils permettant de réagir aux
 * événements souris et clavier que reçoit le canvas. Ce sont les classes
 * qui implémentent V3DCameraController.
 *
 * Un exemple de controller est le V3DDoubleClickController. Ajouté à une 
 * camera, il permet d'être notifié si un double clic a eu lieu dans une de ses
 * zones de dessin. Il est par ailleurs possible de savoir quels sont les objets
 * se trouvant sous le curseur au moment du double clic.
 *
 * La classe V3DContext rassemble principalement des méthodes utilitaires
 * utilisées en interne par les différentes classe de V3DScene. Cependant, cette
 * classe contient aussi quelques méthodes pouvant servir de manière publique :
 *  - Récuperer la liste des élements se trouvant à l'instant présent sous le
 *    curseur de la souris.
 *  - Identifier l'élément le plus proche se trouvant à l'instant présent sous
 *    le curseur de la souris.
 *  - Identifier le binding survolé par la souris.
 *  - Enregistrer des listeners de changement d'objet survolé
 *</pre>
 * @author fberto
 */
public class V3DContext {

    /**
     * Get the top binding currently under the mouse
     * @return top binding
     */
    public V3DCameraBinding getMouseOverCameraBinding() {
        return mouseOverCameraBinding;
    }

    /**
     * Provide the list of V3dElement which are under the mouse. The first is the front element
     * @return list of V3DElement
     */
    public List<V3DElement> getMouseOverlapList() {
        return mouseOverlapList;
    }

    /**
     * Return the element currently at the front under the mouse.
     * If no element is under the mouse, return null.
     * @return front element or null
     */
    public V3DElement getMouseOverlapTop() {

        if (mouseOverlapList.size() > 0) {
            return mouseOverlapList.get(mouseOverlapList.size() - 1);
        } else {
            return null;
        }
    }

    /**
     * Return the element currently at the front under the mouse which is in the
     * whitelist.
     * If none of overlapped elements are in the whitelist, return null.
     * If no element is under the mouse, return null.
     * @param whiteList
     * @return front element or null
     */
    public V3DElement getMouseOverlapTop(List<V3DElement> whiteList) {
        if (mouseOverlapList.size() > 0) {
            for (int i = mouseOverlapList.size() - 1; i >= 0; i--) {
                if (whiteList.contains(mouseOverlapList.get(i))) {
                    return mouseOverlapList.get(i);
                }
            }
            return null;
        } else {
            return null;
        }
    }

    /**
     * Add a mouse overlap listener. The "selectionChange" method of the listenner
     * will be called as soon as the list of overlaped V3DElement change.
     * @param listener
     */
    public void addMouseOverlapListener(V3DMouseOverlapListener listener) {
        mouseOverLapListenerList.add(listener);
    }

    /***
     * Remove the listener from the listener list
     * @param listener
     */
    public void removeMouseOverlapListener(V3DMouseOverlapListener listener) {
        mouseOverLapListenerList.remove(listener);
    }

    //
    //------------------
    // Private stuff
    //------------------
    //
   
    private final ElementComparator elementComparator = new ElementComparator();
    private V3DIdAllocator idAllocator = new V3DIdAllocator();
    private List<FloatValuedElement> mouseOverlapDepthList = new ArrayList<FloatValuedElement>();
    private List<V3DElement> mouseOverlapList = new ArrayList<V3DElement>();
    private List<V3DMouseOverlapListener> mouseOverLapListenerList = new ArrayList<V3DMouseOverlapListener>();
    private V3DCameraBinding mouseOverCameraBinding = null;
    private TextureManager textureManager = new TextureManager();

    /**
     * Internal public method
     * Use by the V3DCanvas to notify a change of top camera binding
     * @param mouseOverCameraBinding new top camera binding
     */
    public void setMouseOverCameraBinding(V3DCameraBinding mouseOverCameraBinding) {
        if (this.mouseOverCameraBinding != mouseOverCameraBinding) {
            this.mouseOverCameraBinding = mouseOverCameraBinding;
            fireMouseOverlapChanged();
        } else {
            this.mouseOverCameraBinding = mouseOverCameraBinding;
        }
    }

    /**
     * Internal public method
     * Used by texture users to access to a texture cache
     * @return texture manager
     */
    public TextureManager getTextureManager() {
        return textureManager;
    }

    /**
     * Internal public method
     * Used by V3DElement to generate there id
     * @return id allocator
     */
    public V3DIdAllocator getIdAllocator() {
        return idAllocator;
    }

    /**
     * Internal public method
     * Used by the cameras to update the list of overlapped elements
     * @param hitsElements list of elements objects and their depth
     */
    public void setMouseOverlapList(FloatValuedElement[] hitsElements) {
        List<FloatValuedElement> newElementsList = Arrays.asList(hitsElements);
        List<FloatValuedElement> oldElementsList = mouseOverlapDepthList;
        if (!(oldElementsList.containsAll(newElementsList) && newElementsList.containsAll(oldElementsList))) {

            Collections.sort(newElementsList, elementComparator);

            mouseOverlapDepthList = newElementsList;
            mouseOverlapList = new ArrayList<V3DElement>(mouseOverlapDepthList.size());

            for (FloatValuedElement elem : mouseOverlapDepthList) {
                if (elem.element != null) {
                    mouseOverlapList.add(elem.element);
                }
            }
            fireMouseOverlapChanged();
        }

    }

    /**
     * Internal public method
     * Notify all mouse overlap listener that there is a change
     */
    private void fireMouseOverlapChanged() {
        for (V3DMouseOverlapListener listener : mouseOverLapListenerList) {
            listener.selectionChange();
        }
    }

    /**
     * Comparator used to sort overlapped elements by depth
     */
    static private class ElementComparator implements Comparator<FloatValuedElement> {

        @Override
        public int compare(FloatValuedElement o1, FloatValuedElement o2) {
            if (o1.value < o2.value) {
                return -1;
            } else if (o1.value > o2.value) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    /**
     * Tuple associating a V3DElement with his depth
     */
    public static class FloatValuedElement {

        public V3DElement element;
        public float value;

        public FloatValuedElement(V3DElement element, float value) {
            this.element = element;
            this.value = value;
        }
    }
}
