import React from "react";
import { Router, Switch, Route, Redirect } from "react-router";
import { Link } from "react-router-dom";
import { createBrowserHistory } from "history";
import Cookies from 'js-cookie'
import LoginHandler from './components/LoginHandler'
import Home from './components/Home'
import CoursePage from './components/CoursePage'
import AppBar from './components/AppBar'
import BaseInstance from './http-client/BaseInstance'

const history = createBrowserHistory()

export default class Routes extends React.Component {
    render() {
        return (
            <Router history={history}>
                <Switch>
                    <Route path="/login" component={LoginHandler} />
                    <Route path="/logout" component={LogoutHandler} />
                    <Route path="/home">
                        <AppBar history={history} />
                        <Home history={history} />
                    </Route>
                    <Route path="/course">
                        <AppBar history={history} />
                        <CoursePage history={history} />
                    </Route>
                    <Route exact path="/" render={() => (
                        <Redirect to="/login" />
                    )} />
                </Switch>
            </Router>
        );
    }
    refresh = () => {
        if (Cookies.get("email") !== undefined) {
            //this.requestAllCourses()
            //this.requestSubscribedCourses()
        }
    }

    componentDidMount = () => {
        this.timer = setInterval(() => this.refresh(), 1000);
    }

    componentWillUnmount = () => {
        clearInterval(this.timer)
        this.timer = null;
    }

    requestAllCourses = () => {
        BaseInstance.post("/getAllCoursesAvailable", {
            email: Cookies.get("email")
        }).then((res) => {
            var list = res.data
            list.sort((a, b) => (a.subject > b.subject) ? 1 : -1)
            Cookies.set("allCourses", list)
        })

    }

    requestSubscribedCourses = () => {
        BaseInstance.post("/getStudentCourses", {
            email: Cookies.get("email")
        }).then((res) => {
            var list = res.data
            list.sort((a, b) => (a.subject > b.subject) ? 1 : -1)
            Cookies.set("subscribedCourses", list)
        })
    }
}

const LogoutHandler = () => {
    Cookies.remove("email")
    Cookies.remove("currentCourse")
    return <Redirect to="/login" />
};




/*
const ProtectedHandler = ({ history }) => {
    const email = Cookies.get("email")
    if (email === undefined) {
      history.push("/login");
    }
    return (
      <div>
        <h6>Protected data for {email}</h6>
        <Link to="/logout">Logout here</Link>
      </div>
    );
  };*/