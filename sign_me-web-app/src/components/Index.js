import React from 'react';
import BaseInstance from '../http-client/BaseInstance'
import Login from './Login'
import Register from './Register'

class Index extends React.Component {

  constructor(props) {
    super(props)
    this.state = {
      login: true,
      credentials: { email: '', password: '', firstName: '', lastName: '' }
    }
  }

  /* Switch to Login/Register forms */

  switchToRegister = () => {
    this.setState({
      login: false,
    })
  }

  switchToLogin = () => {
    this.setState({
      login: true,
    })
  }

  /* OnChanges Credentials */

  onChangeEmail = (e) => {
    let tmp = this.state.credentials
    tmp.email = e.target.value
    this.setState({
      credentials: tmp,
    })
  }

  onChangePassword = (e) => {
    let tmp = this.state.credentials
    tmp.password = e.target.value
    this.setState({
      credentials: tmp,
    })
  }

  onChangeFirstName = (e) => {
    let tmp = this.state.credentials
    tmp.firstName = e.target.value
    this.setState({
      credentials: tmp,
    })
  }

  onChangeLastName = (e) => {
    let tmp = this.state.credentials
    tmp.lastName = e.target.value
    this.setState({
      credentials: tmp,
    })
  }

  /* Functions */

  login = () => {
    BaseInstance.post("login", { email: this.state.credentials.email, password: this.state.credentials.password })
      .then((res) => {
        console.log(res.data)
        if(res.data.email !== undefined) this.props.logUser(res.data.email, res.data.profilePicture, res.data.createdCourses)
      })
  }

  register = () => {
    BaseInstance.post("register", { 
      firstName: this.state.credentials.firstName, 
      lastName: this.state.credentials.lastName, 
      email: this.state.credentials.email, 
      password: this.state.credentials.password })
      .then((res) => {
        if (res.data.email !== undefined) this.switchToLogin()
    })
  }

  render() {
    if (this.state.login) {
      return (
        <Login onChangeEmail={this.onChangeEmail}
          onChangePassword={this.onChangePassword}
          switchToRegister={this.switchToRegister}
          login={this.login}
        />
      )
    }
    else {
      return (
        <Register onChangeFirstName={this.onChangeFirstName}
          onChangeLastName={this.onChangeLastName}
          onChangeEmail={this.onChangeEmail}
          onChangePassword={this.onChangePassword}
          switchToLogin={this.switchToLogin}
          register={this.register}
        />
      )
    }
  }
}
export default Index;