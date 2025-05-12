import { Button, Card, Form, Space } from "antd";
import { useEffect, useState } from "react";
import { Navigate, useNavigate, useParams } from "react-router";
import { closeTicket, sendMessage } from "../Service/UserService";
import TextArea from "antd/es/input/TextArea";

const ReadTicket = ({ user, jwtToken, tickets, isAdmin }) => {
  const { ticketInfo } = useParams();
  console.log(ticketInfo);
  const [t, ticketId] = ticketInfo.split(/-(?=[^ -]*$)/);
  const id = Number(ticketId);
  console.log(id)
  console.log(isAdmin)

  const ticket = isAdmin ? tickets.find((t) => t.id === id) : user.tickets.find((t) => t.id === id);

  console.log(tickets)


  const [form] = Form.useForm();
  const [messages, setMessages] = useState(ticket.messages);

  const navigate = useNavigate();

  const handleSendMessage = (values) => {
    const msg = {
      userId: user.id,
      ticketId: ticket.id,
      mes: values.message
    };

   

    sendMessage(jwtToken, msg)
      .then((response) => {
        setMessages((prevs) => [...prevs, response.data]);
        form.resetFields();
      })
      .catch((error) => {
        console.error(error);
      });
  };

  const handCloseTicket = () => {
    const ClosedTicket = {
      id: ticket.id,
    };

    closeTicket(jwtToken, ClosedTicket)
      .then((response) => {
        form.resetFields();
        
        if(isAdmin){
          navigate("/showAllTickets")
        }

        window.location.reload();
      })
      .catch((error) => {
        console.error(error);
      });
  };

  return (
    <>
      <div>
        <Space direction="vertical" size={16} style={{ width: "100%" }}>
          <Card
            title={`Welcome, ${user.firstname} ${user.lastname}`}
            style={{
              height: 900,
              overflow: "auto",
            }}
          >
            <div>
              <p>Thread: {ticket.thread}</p>
              <p>Category: {ticket.ticketCategory}</p>
              <p>Messages: </p>
              {messages?.map((message) => (
                <p
                  style={{
                    backgroundColor:
                      message.user === user.username ? "purple" : "lightgray",
                    color: message.user === user.username ? "white" : "black",
                    padding: "6px 10px",
                    borderRadius: "6px",
                    marginBottom: "6px",
                    border: "1px solid #ccc",
                    display: "block",
                    width: "fit-content",
                    wordBreak: "break-word",
                  }}
                >
                  {message.user === user.username
                    ? "you: "
                    : `${message.user}: `}
                  {message.message}
                </p>
              ))}

              {ticket.status !== "CLOSED" ? (
                <div>
                  <Form form={form} onFinish={handleSendMessage}>
                    <Form.Item
                      label="Message"
                      name="message"
                      rules={[
                        {
                          required: true,
                          message: "Please input your description!",
                        },
                      ]}
                    >
                      <TextArea rows={4} />
                    </Form.Item>

                    <Button type="primary" htmlType="submit">
                      Submit
                    </Button>
                  </Form>

                  {user.roles.some((role) => role.role === "ROLE_ADMIN") ? (
                    <Button
                      type="primary"
                      htmlType="submit"
                      onClick={handCloseTicket}
                    >
                      Close Ticket
                    </Button>
                  ) : (
                    <div></div>
                  )}
                </div>
              ) : (
                <div></div>
              )}
            </div>
          </Card>
        </Space>
      </div>
    </>
  );
};

export default ReadTicket;
