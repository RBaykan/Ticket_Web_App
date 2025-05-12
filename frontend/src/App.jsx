import React, { useState, useEffect } from "react";
import {
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  UploadOutlined,
  UserOutlined,
  VideoCameraOutlined,
} from "@ant-design/icons";
import { Button, Layout, Menu, theme } from "antd";
import SignUp from "./Component/SignUp";
import ProtectedPages from "./Component/ProtectedPages";
import NotLoginPages from "./Component/NotLoginPages";
import Tickets from "./Component/Tickets";
import ReadTicket from "./Component/ReadTicket";
import Login from "./Component/Login";
import { getTickets, loginWithJwt } from "./Service/UserService";

import { Route, Routes, useNavigate } from "react-router-dom";
import Profile from "./Component/Profile";
import CreateTicket from "./Component/CreateTicket";
import { notification } from "antd";

const { Header, Sider, Content } = Layout;

const menuLoginUser = [
  {
    key: "1",

    label: "Profile",
  },
  {
    key: "2",

    label: "My Tickets",
  },
  {
    key: "3",

    label: "Create Ticket",
  },

  {
    key: "5",

    label: "Log out",
  },
];

const menuLoginAdmin = [
  {
    key: "1",

    label: "Profile",
  },
  {
    key: "2",

    label: "My Tickets",
  },
  {
    key: "3",

    label: "Create Ticket",
  },
  {
    key: "4",

    label: "Manage Users Tickets",
  },
  {
    key: "5",

    label: "Log out",
  },
];

const menuNotLoginItem = [
  {
    key: "1",
    label: "Login",
  },
  {
    key: "2",
    label: "Signup",
  },
];

const App = () => {
  const [selected, setSelected] = useState("1");

  const [user, setUser] = useState(null);

  const jwtToken = localStorage.getItem("userToken");

  const navigate = useNavigate();

  const [isAdmin, setIsAdmin] = useState(false);

  const [tickets, setTickets] = useState(null);

  const [isLoadding, setIsLoading] = useState(true);
  const [menuItem, setMenuItem] = useState(menuNotLoginItem);

  useEffect(() => {
    
    if (jwtToken) {
        
      loginWithJwt(jwtToken)
        .then((response) => {
          setUser(response.data);
          const frameUser = response.data;
         
          if (frameUser) {
            setIsLogin(true);

            const tempAdmin = frameUser.roles.some(
              (role) => role.role === "ROLE_ADMIN"
            );

            if (tempAdmin) {
              setMenuItem(menuLoginAdmin);
            }
            if (!tempAdmin) {
              setMenuItem(menuLoginUser);
            }

            setIsAdmin(tempAdmin);

          
          }
        })
        .catch((error) => {
          if (error.response && error.response.status === 401) {
            notification.error({
              message: "Session Expired",
              description: "Token expired or invalid. Logging out.",
              placement: "topRight",
              duration: 4,
            });
            localStorage.removeItem("jwt");

            setTimeout(() => {
              window.location.reload();
            }, 1500);

    
             
          }

          console.log("çalıştı");
           setMenuItem(menuNotLoginItem);
        })

        .finally(() => {
          setIsLoading(false)
            
        });
    }else{
      setIsLoading(false)
    }
    
  }, [jwtToken]);


  useEffect(() => {
  if (jwtToken && isAdmin) {
    getTickets(jwtToken) 
      .then((response) => {
        setTickets(response.data);
      })
      .catch((error) => {
        console.error("Error fetching tickets:", error);
      });
  }
}, [jwtToken, isAdmin]);

  const handleMenuItem = (e) => {
    const clickedKey = e.key;

    setSelected(clickedKey);

    if (!isLogin) {
      if (clickedKey === "1") {
        navigate("/login");
        window.location.reload();
      }

      if (clickedKey === "2") {
        navigate("/signup");
         window.location.reload();
      }
    }

    if (isLogin) {
      if (clickedKey === "1") {
        navigate("/profile");
         window.location.reload();
      }

      if (clickedKey === "2") {
        navigate("/mytickets");
         window.location.reload();
      }

      if (clickedKey === "3") {
        navigate("/createTicket");
         window.location.reload();
      }

      if (isAdmin) {
        if (clickedKey === "4") {
          navigate("/showAllTickets");
           window.location.reload();
        }
      }

      if (clickedKey === "5") {
        localStorage.removeItem("userToken");
        navigate("/");
        window.location.reload();
      }
    }
  };

  const [isLogin, setIsLogin] = useState(false);

  const [collapsed, setCollapsed] = useState(false);
  const {
    token: { colorBgContainer, borderRadiusLG },
  } = theme.useToken();



  const setSelectMenuItem = () => {
    const path = location.pathname;

    if (!isLogin) {
      if (path === "/login") setSelected("1");
      else if (path === "/signup") setSelected("2");
    } else {
      if (path === "/profile" || path === "/") setSelected("1");
      else if (path === "/mytickets") setSelected("2");
      else if (path === "/createTicket") setSelected("3");
      else if (path === "/showAllTickets") setSelected("4");
    }
  }

  useEffect(() => {
    setSelectMenuItem();
  }, [location.pathname, isLogin, isAdmin]);

  return (
    <>
      <Layout style={{ minHeight: "100vh" }}>
        {" "}
        {}(
        <Sider trigger={null} collapsible collapsed={collapsed}>
          <div className="demo-logo-vertical" />
          <Menu
            items={menuItem}
            onClick={handleMenuItem}
            theme="dark"
            mode="inline"
            selectedKeys={[selected]}
          />
        </Sider>
        )
        <Layout>
          <Header style={{ padding: 0, background: colorBgContainer }}>
            <Button
              type="text"
              icon={collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
              onClick={() => setCollapsed(!collapsed)}
              style={{
                fontSize: "16px",
                width: 64,
                height: 64,
              }}
            />
          </Header>
          <Content
            style={{
              margin: "24px 16px",
              padding: 24,
              minHeight: 280,
              background: colorBgContainer,
              borderRadius: borderRadiusLG,
            }}
          >
            {!isLoadding ? <Routes>
              <Route
                element={
                  <ProtectedPages isLogin={isLogin} isLoadding={isLoadding} />
                }
              >
                <Route path="/" element={<Profile user={user} />} />
                <Route path="/profile" element={<Profile user={user} />} />
                <Route
                  path="/mytickets"
                  element={
                    <Tickets user={user} isAdmin={false} tickets={null} />
                  }
                />
                <Route
                  path={isAdmin ? "/showAllTickets" : "/"}
                  element={
                    <Tickets user={user} isAdmin={isAdmin} tickets={tickets} />
                  }
                />
                <Route
                  path="/createTicket"
                  element={<CreateTicket user={user} jwtToken={jwtToken} />}
                />
                <Route
                  path="/readTicket/:ticketInfo"
                  element={
                    <ReadTicket
                      user={user}
                      jwtToken={jwtToken}
                      tickets={tickets}
                      isAdmin={isAdmin}
                    />
                  }
                />
              </Route>

              <Route
                element={
                  <NotLoginPages isLogin={isLogin} isLoadding={isLoadding} />
                }
              >
                <Route path="/login" element={<Login />} />
                <Route path="/signup" element={<SignUp />} />
              </Route>
            </Routes> : <div>Loading</div>}
          </Content>
        </Layout>
      </Layout>
    </>
  );
};

export default App;
