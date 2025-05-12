import { Outlet, Navigate } from 'react-router-dom'

const NotLoginPages = ({ isLogin, isLoading }) => {

  



    return isLogin === false ? <Outlet/> : <Navigate to="/"/>



}


export default NotLoginPages;
 