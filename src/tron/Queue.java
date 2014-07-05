/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tron;

/**
 *
 * @author Isaac
 */
public class Queue<E> {
    private class Node<E> {
        E data;
        Node next;
        public Node(E setdata, Node setnext)
        {
            data=setdata;
            next=setnext;
        }
    }
    Node<E> head,tail;
    public Queue()
    {
        head=null;
        tail=null;
    }
    public void push_back(E data)
    {
        Node add=new Node(data,null);
        if (tail!=null)
            tail.next=add;
        tail=add;
        if (head==null)
            head=add;
    }
    
    public E pop_front() {
        if (head==null)
        {
            System.out.println("Popping null head");
            return null;
        }
        Node pop=head;
        head=pop.next;
        if (head==null)
            tail=null;
        return (E) pop.data;
    }
    public void purge(E delete) {
        Node cur=head;
        Node last=null;
        while (cur!=null)
        {
            if (cur.data.equals(delete))
            {
                if (last==null)
                    head=cur.next;
                else
                    last.next=cur.next;
            }
            last=cur;
            cur=cur.next;
        }
    }
    public boolean notEmpty() {
        return head!=null;
    }
    public boolean empty() {
        return head==null;
    }
    public void clear()
    {//fucking java does all the deletion for you
        //like some coddling mother
        head=null;
        tail=null;
    }
}
