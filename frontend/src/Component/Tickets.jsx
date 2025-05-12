import { Card, Form, Select, Space } from "antd";
import { useState } from "react";
import { useNavigate } from "react-router";


const Ticket = ({ user, isAdmin, tickets }) => {

  

  return (
    <>
      <div>
        <Space direction="vertical" size={16} style={{ width: "100%" }}>
          <Card
            title= "My Tickets"
            style={{
              height: 900,
              overflow: "auto",
            }}
          >
              <ManageTicket
                  tickets={isAdmin ? tickets : user.tickets} user={user}
                />
          </Card>
        </Space>
      </div>
    </>
  );
};



const categoires = [
  { value: "GENERAL", label: "GENERAL" },
  { value: "HELP", label: "HELP" },
  { value: "Technical_Support", label: "Technical_Support" },
  { value: "LOGIN_ISSUE", label: "LOGIN_ISSUE" },
  { value: "FEEDBACK", label: "FEEDBACK" },
];

const categoriesFilter = [
  { value: "ALL", label: "ALL CATEGORY" },
  { value: "GENERAL", label: "GENERAL" },
  { value: "HELP", label: "HELP" },
  { value: "Technical_Support", label: "Technical_Support" },
  { value: "LOGIN_ISSUE", label: "LOGIN_ISSUE" },
  { value: "FEEDBACK", label: "FEEDBACK" },
];

const ticketStatusFilter = [
  { value: "ALL", label: "ALL STATUS" },
  { value: "OPEN", label: "OPEN" },
  { value: "REPLIED", label: "REPLIED" },
  { value: "CLOSED", label: "CLOSED" },
];

const colorDictionary = {
  GENERAL: "#FFF176",          
  HELP: "#64B5F6",              
  Technical_Support: "#B0BEC5", 
  LOGIN_ISSUE: "#EF5350",      
  FEEDBACK: "#BA68C8",        
};

const layout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 16 },
};



const ManageTicket = ({ tickets, user }) => {
  const [selectedCategory, setSelectedCategory] = useState("ALL");
  const [selectedStatus, setSelectedStatus] = useState("ALL");

  const handleCategoryChange = (value) => {
    setSelectedCategory(value);
  };

  const handleStatusChange = (value) => {
    setSelectedStatus(value);
  };

  const filteredTickets = tickets?.filter((ticket) => {
    const categoryMatch =
      selectedCategory === "ALL" || ticket.ticketCategory === selectedCategory;
    const statusMatch =
      selectedStatus === "ALL" || ticket.status === selectedStatus;
    return categoryMatch && statusMatch;
  });

  const navigate = useNavigate();

  const handReadTicket = (ticket) => {

    navigate(`/readTicket/${ticket.thread}-${ticket.id}`);
    
  };

  return (
    <>
        <div>
          <Form layout="inline" style={{ marginBottom: 20 }}>
            <Form.Item label="Category">
              <Select
                placeholder="Select a category"
                value={selectedCategory}
                options={categoriesFilter}
                onChange={handleCategoryChange}
              />
            </Form.Item>

            <Form.Item label="Status">
              <Select
                placeholder="Select a status"
                value={selectedStatus}
                options={ticketStatusFilter}
                onChange={handleStatusChange}
              />
            </Form.Item>
          </Form>

          {filteredTickets?.length > 0 ? (
            filteredTickets.map((ticket) => (
              <Card
                hoverable
                title={"Thread: " + ticket.thread}
                style={{
                  width: 550,
                  marginBottom: 20,
                  border: "2px solid #1890ff",
                  borderRadius: "8px",
                  boxShadow: "0 2px 8px rgba(0, 0, 0, 0.1)",
                }}
                onClick={() => handReadTicket(ticket)}
              >
                <p
                  style={{
                    backgroundColor:
                      ticket.status === "REPLIED"
                        ? "lightgreen"
                        : ticket.status === "CLOSED"
                        ? "lightcoral"
                        : "transparent",
                    border: "1px solid #ccc",
                    display: "inline-block",
                    padding: "4px 8px",
                    borderRadius: "4px",
                  }}
                >
                  {ticket.status}
                </p>
                <p
                  style={{
                    backgroundColor: colorDictionary[ticket.ticketCategory],
                    color: "#004080",
                    padding: "4px 10px",
                    borderRadius: "6px",
                    display: "inline-block",
                    border: "1px solid #b3d7ff",
                    fontWeight: "500",
                    marginBottom: "6px",
                  }}
                >
                  {ticket.ticketCategory}
                </p>

                <p
                  style={{
                    backgroundColor: "#f5f5f5",
                    padding: "10px 14px",
                    border: "1px solid #d9d9d9",
                    borderRadius: "8px",
                    fontStyle: "italic",
                    color: "#333",
                    lineHeight: "1.5",
                    maxWidth: "90%",

                    marginBottom: "8px",
                  }}
                >
                  {ticket?.messages[0].message}
                </p>
                <p>user: {ticket.username}</p>
              </Card>
            ))
          ) : (
            <p>No tickets match the selected filters.</p>
          )}
        </div>
      
    </>
  );
};

export default Ticket;
