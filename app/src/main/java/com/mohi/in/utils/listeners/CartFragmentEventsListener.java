package com.mohi.in.utils.listeners;

import com.mohi.in.model.CartModel;

//Listener to Listen the events on Cart Fragment (Increase Quantity, Decrease Quantity, Delete Product from cart)
public interface CartFragmentEventsListener {
    public void cartEventListener(String event,CartModel model);
}
