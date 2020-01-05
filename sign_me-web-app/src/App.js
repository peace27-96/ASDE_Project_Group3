import React from 'react';
import Index from './components/Index';
import AppBar from './components/AppBar';
import Home from './components/Home';
import CoursePage from './components/CoursePage';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Redirect
} from "react-router-dom";
import BaseInstance from './http-client/BaseInstance';


export default class App extends React.Component {

  constructor() {
    super()
    this.state = {
      logged: false,
      credentials: {},
      picturePath: "",
      createdCourses: [],
      allCourses: [],
      currentCourse: null,
      homeRender: false,
      courseRender: false
    }
  }

  logUser = (data) => {
    this.setState({
      logged: true,
      credentials: {"email": data.email, "firstName":data.firstName, "lastName":data.lastName},
      picturePath: data.profilePicture,
      createdCourses: data.createdCourses,
      homeRender: true
    })
    this.requestAllCourses()
    //this.props.history.push('/home');
  }

  getCredentials = () => {
    return this.state.credentials;
  }

  getUser = () => {
    return this.state.credentials.email
  }

  getPicture = () => {
    return this.state.picturePath
  }

  getCourses = () => {
    return this.state.createdCourses
  }

  addCourse = (id, name) => {
    var courses = this.state.createdCourses
    courses.push({ "courseId": id, "subject": name })
    this.setState({
      createdCourses: courses,
    })
  }

  getCurrentCourse = () => {
    return this.state.currentCourse;
  }

  goToCoursePage = (course) => {
    console.log("goto")
    console.log(course)
    this.setState({
      homeRender: false,
      courseRender: true,
      currentCourse: course
    })
  }

  renderHome = () => {
    if (this.state.logged && this.state.homeRender) {
      return <Redirect to='/home' />
    }
  }

  renderCoursePage = () => {
    if (this.state.logged && this.state.courseRender) {
      return <Redirect to='/course_page' />
    }
  }

  requestAllCourses = () => {
    BaseInstance.post("/getAllCoursesAvailable", {
      email: this.state.credentials.email      
    }).then((res) => {
      this.setState({
        allCourses: res.data
      })
    })
  }

  getAllCourses = () => {
    console.log(this.state.allCourses)
    return this.state.allCourses
  }

  render() {
    return (
      <Router >
        <div>
          {this.renderHome()}
          {this.renderCoursePage()}


          <Switch>
            <Route path="/home">
              <AppBar getAllCourses={this.getAllCourses} getUser={this.getUser}/>
              <Home getUser={this.getUser} getPicture={this.getPicture} 
                    getCourses={this.getCourses} addCourse={this.addCourse} 
                    goToCoursePage={this.goToCoursePage} getCredentials={this.getCredentials}
              />
            </Route>
            <Route exact path="/">
              <Index logUser={this.logUser} />
            </Route>
            <Route path="/course_page">
              <AppBar getAllCourses={this.getAllCourses} getUser={this.getUser}/>
              <CoursePage getAllCourses={this.getAllCourses} getCurrentCourse={this.getCurrentCourse}/>
            </Route>
          </Switch>
        </div>
      </Router>

    );
  }
}
