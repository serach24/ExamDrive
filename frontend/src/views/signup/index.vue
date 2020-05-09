<template>
  <div class="signUp-container">
    <el-form
      ref="signUpForm"
      :model="signUpForm"
      :rules="signUpRules"
      class="signUp-form"
      auto-complete="on"
      label-position="left"
    >
      <div class="title-container">
        <h3 class="title">Sign Up</h3>
      </div>

      <el-form-item prop="username">
        <span class="svg-container">
          <svg-icon icon-class="user"/>
        </span>
        <el-input
          ref="username"
          v-model="signUpForm.username"
          placeholder="Username"
          name="username"
          type="text"
          tabindex="1"
          auto-complete="on"
        />
      </el-form-item>

      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password"/>
        </span>
        <el-input
          :key="passwordType"
          ref="password"
          v-model="signUpForm.password"
          :type="passwordType"
          placeholder="Password"
          name="password"
          tabindex="2"
          auto-complete="on"
          @keyup.enter.native="handleSignUp"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'"/>
        </span>
      </el-form-item>

      <el-form-item prop="email">
        <span class="svg-container">
          <svg-icon icon-class="user"/>
        </span>
        <el-input
          ref="email"
          v-model="signUpForm.email"
          placeholder="Email"
          name="email"
          type="text"
          tabindex="3"
          auto-complete="on"
        />
      </el-form-item>

      <el-form-item prop="school">
        <span class="svg-container">
          <svg-icon icon-class="user"/>
        </span>
        <el-input
          ref="school"
          v-model="signUpForm.school"
          placeholder="School"
          name="school"
          type="text"
          tabindex="4"
          auto-complete="on"
        />
      </el-form-item>

      <el-form-item prop="realName">
        <span class="svg-container">
          <svg-icon icon-class="user"/>
        </span>
        <el-input
          ref="realName"
          v-model="signUpForm.realName"
          placeholder="Real Name"
          name="realName"
          type="text"
          tabindex="5"
          auto-complete="on"
        />
      </el-form-item>
      <el-button
        :loading="loading"
        type="primary"
        style="width:100%;margin-bottom:60px;"
        @click.native.prevent="handleSignUp"
      >Submit</el-button>
    </el-form>
  </div>
</template>

<script>
import { signup } from "@/api/user";
import { message } from "element-ui";
export default {
  name: "SignUp",
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!value) {
        callback(new Error("Please enter the username"));
      } else {
        callback();
      }
    };
    const validatePassword = (rule, value, callback) => {
      if (!value) {
        callback(new Error("Please enter the password"));
      } else {
        if (value.length < 6) {
          call;
        }
        callback();
      }
    };
    const validateEmail = (rule, value, callback) => {
      let reg =  /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/
      if (!value) {
        callback(new Error("Please enter the email"))
      } else if (!reg.test(value)) {
        callback(new Error("Please confirm the email format"))
      } else{
        callback();
      }
    };
    const validateSchool = (rule, value, callback) => {
      if (!value) {
        callback(new Error("Please enter your school"));
      } else {
        callback();
      }
    };
    const validateRealName = (rule, value, callback) => {
      if (!value) {
        callback(new Error("Please enter your real name"));
      } else {
        callback();
      }
    };
    return {
      signUpForm: {
        username: "",
        password: "",
        email: "",
        school: "",
        realName: ""
      },
      signUpRules: {
        username: [
          { required: true, trigger: "blur", validator: validateUsername }
        ],
        password: [
          { required: true, trigger: "blur", validator: validatePassword }
        ],
        email: [{ required: true, trigger: "blur", validator: validateEmail }],
        school: [
          { required: true, trigger: "blur", validator: validateSchool }
        ],
        realName: [
          { required: true, trigger: "blur", validator: validateRealName }
        ]
      },
      loading: false,
      passwordType: "password",
      redirect: undefined
    };
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true
    }
  },
  methods: {
    showPwd() {
      if (this.passwordType === "password") {
        this.passwordType = "";
      } else {
        this.passwordType = "password";
      }
      this.$nextTick(() => {
        this.$refs.password.focus();
      });
    },
    handleSignUp() {
      this.$refs.signUpForm.validate(valid => {
        if (valid) {
          console.log(this.signUpForm);
          this.loading = true;
          signup(this.signUpForm)
            .then(() => {
              this.loading = false;
              this.$router.push({ path: this.redirect || "/login" });
              this.$message({
                message: "Signup Success, Please Login",
                type: 'success'
              });
            })
            .catch(() => {
              this.loading = false;
            });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    }
  }
};
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg: #283443;
$light_gray: #fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .signUp-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.signUp-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style lang="scss" scoped>
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;

.signUp-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  .signUp-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
}
</style>
