import { Card, Space } from "antd";
import { useEffect } from "react";


const Profile = ({ user }) => {
 
  
  useEffect(() => {

 

  }, [user]);

  return (
    <>
      <div>
        <Space direction="vertical" size={16} style={{ width: "100%" }}>
          <Card
            title={`Welcome, ${user.firstname} ${user.lastname}`}
            style={{
              height: 200,
              overflow: "auto",
            }}
          >
            <>
              <p>
                username: <b>{user.username}</b>
              </p>
            </>
          </Card>
        </Space>
      </div>
    </>
  );
};

export default Profile;
