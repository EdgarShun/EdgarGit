import React, { Component } from 'react';
import { AppRegistry, Text, TextInput, View,Button,NativeModules } from 'react-native';

export default class study extends Component {

_onPressButton() {
    NativeModules.StartVoice.show();
}

  render() {
    return (
      <View style={{
        flex: 1,
        flexDirection: 'column',
        justifyContent: 'center',
        }}>

        <Button
        onPress={this._onPressButton}
        title="点击我"
        />
      </View>
    );
  }
}

AppRegistry.registerComponent('study', () => study);