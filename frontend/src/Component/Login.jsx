import React, { useState, useEffect } from "react";
import { Button, Checkbox, Form, Input } from "antd";
import { loginUser } from "../Service/UserService";
import * as yup from "yup";
import { useNavigate } from "react-router";

const schema = yup.object().shape({
  username: yup
    .string()
    .required("Username is required")
    .min(3, "Username must be at least 3 characters long"),
  password: yup
    .string()
    .required("Password is required")
    .min(8, "Password must be at least 8 characters long"),
});

const Login = () => {
  const navigate = useNavigate();
    const [form] = Form.useForm();
  const yupSync = {
    async validator({ field }, value) {
      const formValues = form.getFieldsValue();

      await schema.validateSyncAt(field, { ...formValues, [field]: value });
    },
  };

  const onFinish = (values) => {
    loginUser(values)
      .then((response) => {
        const token = response.data;
        localStorage.setItem("userToken", token);
        navigate("/")
        window.location.reload();
      })
      .catch((error) => {
        if (error.status == 404) {
          setInvalidUser(true);
        }
      });
  };
  const onFinishFailed = (errorInfo) => {
    console.log("Failed:", errorInfo);
  };

  const [invalidUser, setInvalidUser] = useState(false);

  return (
    <>
      {invalidUser ? (
        <h2 style={{ color: "red", textAlign: "center" }}>Invaild User</h2>
      ) : (
        <div></div>
      )}
      <Form
        name="basic"
        labelCol={{ span: 8 }}
        wrapperCol={{ span: 16 }}
        style={{ maxWidth: 600 }}
        initialValues={{ remember: true }}
        onFinish={onFinish}
        onFinishFailed={onFinishFailed}
        autoComplete="off"
      >
        <Form.Item
          label="Username"
          name="username"
          rules={[{ validator: yupSync.validator }]}
        >
          <Input />
        </Form.Item>

        <Form.Item
          label="Password"
          name="password"
          rules={[{ validator: yupSync.validator }]}
        >
          <Input.Password />
        </Form.Item>

        <Form.Item label={null}>
          <Button type="primary" htmlType="submit">
            Login
          </Button>
        </Form.Item>
      </Form>
    </>
  );
};

export default Login;
