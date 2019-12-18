import React from 'react';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import WorkIcon from '@material-ui/icons/Work';
import CardContent from '@material-ui/core/CardContent';
import Card from '@material-ui/core/Card';
import ListItemIcon from '@material-ui/core/ListItemIcon';

export default class FolderList extends React.Component {
    
    render() {
        var array = [...Array(10).keys()]; // [0,...,9]
        return (
            <div style={{ padding: "10px" }}>
                <Card style={{ maxHeight: 250, overflow: 'auto', width: '90%' }}>
                    <CardContent>
                        <List>
                            {
                                array.map(item => (
                                    <ListItem button>
                                        <ListItemIcon>
                                            <WorkIcon />
                                        </ListItemIcon>
                                        <ListItemText primary={`${this.props.type} ${item}`} />
                                    </ListItem>
                                ))
                            }
                        </List>
                    </CardContent>
                </Card>
            </div>
        );
    }
}