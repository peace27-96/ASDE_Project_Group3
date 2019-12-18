import React from 'react';
import Index from './components/Index';
import AppBar from './components/AppBar';
import Home from './components/Home';
import CoursePage from './components/CoursePage';


export default class App extends React.Component {

  constructor() {
    super()
    this.state = {
      logged: false,
      userId: "",
      picturePath: "",
      courses: []
    }
  }

  logUser = (userId, path, courses) => {
      this.setState({
        logged: true,
        userId: userId,
        picturePath: path,
        courses: courses
      })
  }

  getUser = () => {
    return this.state.userId
  }

  getPicture = () => {
    return this.state.picturePath
  }

  getCourses = () => {
    return this.state.courses
  }

  addCourse = (id, name) => {
    var courses = this.state.courses
    courses.push( {"courseId":id, "subject":name} )
    this.setState({
      courses : courses
    })
  }
  
  render() {
    if(this.state.logged) 
      return(
        <div>
          <AppBar />
          <Home getUser={this.getUser} getPicture={this.getPicture} getCourses={this.getCourses} addCourse={this.addCourse}/>
        </div>
      );
    else 
        return(
          <div>
            <Index logUser={this.logUser}/>
          </div>
        );/*
        return(
          <div>
            <AppBar/>
            <Home/>
          </div>
        );*/
  }
  
}
