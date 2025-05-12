import {
  Button,
  Form,
  Input,
  Select

} from "antd";
import { createTicketWithJWt } from "../Service/UserService";
import TextArea from "antd/es/input/TextArea";
import { useNavigate } from "react-router";

const CreateTicket = ({ user, jwtToken }) => {

    const [form] = Form.useForm();

   const navigate = useNavigate();

  const categoires = [
    { value: "GENERAL", label: "GENERAL" },
    { value: "HELP", label: "HELP" },
    { value: "Technical_Support", label: "Technical_Support" },
    { value: "LOGIN_ISSUE", label: "LOGIN_ISSUE" },
    { value: "FEEDBACK", label: "FEEDBACK" },
  ];

  const layout = {
    labelCol: { span: 8 },
    wrapperCol: { span: 16 },
  };

  const onFinish = (values) => {
    const ticket = {
      userId: user.id,
      thread: values.thread,
      message: values.message,
      ticketCategory: values.ticketCategory,
    };
    createTicketWithJWt(ticket, jwtToken)
      .then((_response) => {
        form.resetFields();
        navigate("/myTickets")
        window.location.reload()

      })
      .catch((_error) => {
        console.log("error");
      });
  };

  return (
    <>
    
      <Form {...layout} onFinish={onFinish}>
        <Form.Item
          label="Category"
          name="ticketCategory"
          rules={[{ required: true, message: "Please input your username!" }]}
        >
          <Select
            placeholder="Select a category"
            optionFilterProp="label"
            options={categoires}
          />
        </Form.Item>

        <Form.Item
          label="Thread"
          name="thread"
          rules={[{ required: true, message: "Please input your username!" }]}
        >
          <Input></Input>
        </Form.Item>
        <Form.Item
          label="Message"
          name="message"
          rules={[
            { required: true, message: "Please input your description!" },
          ]}
        >
          <TextArea rows={4} />
        </Form.Item>

        <Button type="primary" htmlType="submit">
          Submit
        </Button>
      </Form>
    </>
  );
};

export default CreateTicket;
