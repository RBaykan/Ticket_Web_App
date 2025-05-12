import React, { useState } from "react";
import { Button, Form, Input, message } from "antd";
import * as yup from "yup";
import { createUser } from "../Service/UserService";
import { useNavigate } from "react-router";

const schema = yup.object().shape({
  firstname: yup.string().required("First name is required"),
  lastname: yup.string().required("Last name is required"),
  username: yup
    .string()
    .required("Username is required")
    .min(3, "Username must be at least 3 characters long"),
  password: yup
    .string()
    .required("Password is required")
    .min(8, "Password must be at least 8 characters long"),
  repassword: yup
    .string()
    .oneOf([yup.ref("password")], "Passwords must match")
    .required("Password confirmation is required"),
});

const SignUp = () => {
  const navigate = useNavigate();
  const [messageApi, contextHolder] = message.useMessage();
  const [form] = Form.useForm();
  const [sameUser, setSameUser] = useState(false);
  const [hideButton, setHideButton] = useState(false);

  const info = () => {
    messageApi.info("Account is create");
  };

  const yupSync = {
    async validator({ field }, value) {
      const formValues = form.getFieldsValue();

      await schema.validateSyncAt(field, { ...formValues, [field]: value });
    },
  };

  const onFinish = (values) => {
    createUser(values)
      .then((response) => {
        setHideButton(true);
        info();
        setTimeout(() => {
          navigate("/");
          window.location.reload();
        }, 1500);
      })
      .catch((error) => {
        if (error.status === 500) {
          setSameUser(true);
        }
      });
  };

  const onFinishFailed = (error) => {};

  return (
    <>
      {sameUser ? (
        <h2 style={{ color: "red", textAlign: "center" }}>This user is already registered</h2>
      ) : (
        <div></div>
      )}

      <Form
        form={form}
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
          label="Firtsname"
          name="firstname"
          rules={[{ validator: yupSync.validator }]}
        >
          <Input />
        </Form.Item>

        <Form.Item
          label="Lastname"
          name="lastname"
          rules={[{ validator: yupSync.validator }]}
        >
          <Input />
        </Form.Item>

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

        <Form.Item
          label="Password"
          name="repassword"
          rules={[{ validator: yupSync.validator }]}
        >
          <Input.Password />
        </Form.Item>

        <Form.Item wrapperCol={{ offset: 8, span: 16 }}>
          {contextHolder}
          <Button
            type="primary"
            htmlType="submit"
            style={{ display: hideButton ? "none" : "inline-block" }}
          >
            Signup
          </Button>
        </Form.Item>
      </Form>
    </>
  );
};

export default SignUp;
