import { Outlet, Navigate } from 'react-router-dom'

const ProtectedPages = ({ isLogin, isLoading }) => {

  

  return isLogin ? <Outlet /> : <Navigate to="/login" />;



}


export default ProtectedPages;
 