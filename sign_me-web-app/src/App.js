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
      subscribedCourses: [],
      allCourses: [],
      currentCourse: null,
      homeRender: false,
      courseRender: false
    }
  }

  logUser = (data) => {
    var list = data.followingCourses;
    list.sort((a, b) => (a.subject > b.subject) ? 1 : -1)
    this.setState({
      logged: true,
      credentials: { "email": data.email, "firstName": data.firstName, "lastName": data.lastName },
      picturePath: data.profilePicture,
      createdCourses: data.createdCourses,
      subscribedCourses: list,
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
    console.log("go to")
    console.log(course)
    this.setState({
      homeRender: false,
      courseRender: true,
      currentCourse: course
    })
  }

  requestAllCourses = () => {
    BaseInstance.post("/getAllCoursesAvailable", {
      email: this.state.credentials.email
    }).then((res) => {
      var list = res.data
      list.sort((a, b) => (a.subject > b.subject) ? 1 : -1)
      this.setState({
        allCourses: list
      })
    })

  }

  refresh = () => {
    if (this.state.logged) {
      this.requestAllCourses()
      this.requestSubscribedCourses()
    }
  }

  componentDidMount() {
    this.timer = setInterval(() => this.refresh(), 1000);
  }

  componentWillUnmount() {
    clearInterval(this.timer)
    this.timer = null;
  }

  getAllCourses = () => {
    console.log(this.state.allCourses)
    return this.state.allCourses
  }

  getSubscribedCourses = () => {
    return this.state.subscribedCourses;
  }

  requestSubscribedCourses = () => {
    BaseInstance.post("getStudentCourses", {
      email: this.getUser()
    }).then((res) => {
      var list = res.data
      list.sort((a, b) => (a.subject > b.subject) ? 1 : -1)
      this.setState({
        subscribedCourses: list
      })
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

  render() {
    return (
      <Router >
        <div>
          {this.renderHome()}
          {this.renderCoursePage()}


          <Switch>
            <Route path="/home">
              <AppBar getAllCourses={this.getAllCourses} getUser={this.getUser} />
              <Home getUser={this.getUser} getPicture={this.getPicture}
                getCourses={this.getCourses} addCourse={this.addCourse}
                goToCoursePage={this.goToCoursePage} getCredentials={this.getCredentials}
                getSubscribedCourses={this.getSubscribedCourses}
              />
            </Route>
            <Route exact path="/">
              <Index logUser={this.logUser} />
            </Route>
            <Route path="/course_page">
              <AppBar getAllCourses={this.getAllCourses} getUser={this.getUser} />
              <CoursePage getAllCourses={this.getAllCourses}
                getCurrentCourse={this.getCurrentCourse} />
            </Route>
          </Switch>
        </div>
      </Router>

    );
  }
}
