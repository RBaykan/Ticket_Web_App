import React from 'react';
import axios from 'axios';


const BASE_URL = 'http://localhost:8080/api/user'



export const createUser = (user) => axios.post(BASE_URL + '/register', user); 
export const loginUser = (user) => axios.post(BASE_URL + '/login', user); 
export const loginWithJwt = (jwtToken) => axios.get(BASE_URL + '/loginJWT', {
    headers: {
        'Authorization': `Bearer ${jwtToken}`
    }
});

export const createTicketWithJWt = (ticket, jwtToken) => axios.post(BASE_URL + '/createTicket', ticket,{
    headers: {
        'Authorization': `Bearer ${jwtToken}`
    }
});



export const getTickets = (jwtToken) => axios.get(BASE_URL + '/tickets',{
    headers: {
        'Authorization': `Bearer ${jwtToken}`
    }
});

export const sendMessage = (jwtToken, message) => axios.put(
    BASE_URL + '/sendMessage', message, 
    {
      headers: {
        'Authorization': `Bearer ${jwtToken}`
      }
    }
  );

export const closeTicket = (jwtToken, id) => axios.put(
    BASE_URL + '/closeTicket', id, 
    {
      headers: {
        'Authorization': `Bearer ${jwtToken}`
      }
    }
  );
 

