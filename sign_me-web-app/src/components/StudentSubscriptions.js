import React from 'react';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import WorkIcon from '@material-ui/icons/Work';
import CardContent from '@material-ui/core/CardContent';
import Card from '@material-ui/core/Card';
import BaseInstance from '../http-client/BaseInstance';
import CheckIcon from '@material-ui/icons/Check';
import ClearOutlinedIcon from '@material-ui/icons/ClearOutlined';
import Cookies from 'js-cookie'

export default class StudentSubscriptions extends React.Component {

    constructor() {
        super();
        this.state = {
            subscriptions: [],
            loaded: false
        }
        this.getSubscriptions();
    }

    confirmSubscription = (studentId) => {
        BaseInstance.post("confirmSubscription", {
            student: studentId,
            courseId: JSON.parse(Cookies.get("currentCourse")).courseId
        }).then((res) => {
           
        })
    }

    deleteSubscription = (studentId) => {
        BaseInstance.post("deleteSubscription", {
            student: studentId,
            courseId: JSON.parse(Cookies.get("currentCourse")).courseId
        }).then((res) => {
           
        })
    }

    getSubscriptions = () => { 
        BaseInstance.post("getSubscriptionRequests", {
            courseId: JSON.parse(Cookies.get("currentCourse")).courseId
        }).then((res) => {
            var students=[];
            for(var i = 0; i < res.data.length; i++) {
                var student = { "firstName": res.data[i].student.firstName, 
                                "lastName": res.data[i].student.lastName, 
                                "email": res.data[i].student.email };
                students.push(student);
            }
            this.setState({
                subscriptions: students,
                loaded: true
            })
        })
    }

    componentDidMount() {
        this.timer = setInterval(()=> this.getSubscriptions(), 1000);
    }
    
    componentWillUnmount() {
        clearInterval(this.timer)
        this.timer = null;
    }
    
    render() {
        if (this.state.loaded && this.state.subscriptions.length!=0) {
            return (
                <div style={{ padding: "10px" }}>
                    <Card style={{ maxHeight: 250, overflow: 'auto', width: '90%' }}>
                        <CardContent>
                            <List>
                                {
                                    this.state.subscriptions.map(item => (
                                        <ListItem>
                                            <ListItemText style={{ width: 50 }} primary={`${item.firstName} ${item.lastName}`} />
                                            <ListItem button style={{ width: 25 }} button onClick={ () => {this.confirmSubscription(item.email)} }> <CheckIcon/> </ListItem>
                                            <ListItem button style={{ width: 25 }} button onClick={ () => {this.deleteSubscription(item.email)} }> <ClearOutlinedIcon/> </ListItem>
                                        </ListItem>
                                    ))
                                }
                            </List>
                        </CardContent>
                    </Card>
                </div>
            );
        } else {
            return (
                <div style={{ padding: "10px" }}>
                    <Card style={{ maxHeight: 250, overflow: 'auto', width: '90%' }}>
                        <CardContent>
                            <ListItem>
                                <ListItemText>No Subscriptions Requests</ListItemText>    
                            </ListItem>    
                        </CardContent>
                    </Card>
                </div>
            );
        }
    }
}